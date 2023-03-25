package rps.server.command;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import rps.server.command.annotations.Input;
import rps.server.game.UserState;
import rps.server.model.UserInfo;
import rps.server.network.ClientConnection;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandExecutor {
  Object holder;
  Method method;
  MethodHandle methodHandle;
  String command;
  String description;
  String category;
  boolean announceCommandsOnSuccess;
  boolean announceToClient;
  Map<String, Class> allowedParams;
  Set<UserState> allowedStates;

  public boolean canExecute(ClientConnection connection) {
    if (connection.isLogged()) {
      return allowedStates.contains(connection.getUser().getUserState());
    } else {
      return isForUnauthorized();
    }
  }

  public boolean isForUnauthorized() {
    return allowedStates.isEmpty();
  }

  public boolean execute(ClientConnection connection, String command, ParameterMap parameterMap)
      throws Throwable {
    var paramValues = new ArrayList<>(method.getParameterCount());
    for (var i = 0; i < method.getParameterCount(); i++) {
      var param = method.getParameters()[i];
      var paramType = param.getType();
      Object value = null;

      if (param.isAnnotationPresent(Input.class)) {
        value = parameterMap.getParam(param.getAnnotation(Input.class).value());
      } else if (paramType.isInstance(parameterMap)) {
        value = parameterMap;
      } else if (paramType.isInstance(connection)) {
        value = connection;
      } else if (paramType == UserInfo.class) {
        value = connection.getUser();
      } else if (paramType == String.class) {
        value = command;
      } else {
        throw new IllegalStateException("Unknown parameter " + param);
      }

      paramValues.add(value);
    }
    var invokeResult = methodHandle.invokeWithArguments(paramValues);
    if (invokeResult instanceof Boolean b) {
      return ((Boolean) invokeResult).booleanValue();
    } else {
      return true;
    }
  }
}
