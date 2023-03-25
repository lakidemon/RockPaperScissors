package rps.server.command;

import com.google.common.base.Preconditions;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import rps.networking.packets.server.ServerCommands;
import rps.networking.packets.server.ServerResponse;
import rps.server.command.annotations.Command;
import rps.server.command.annotations.CommandHandler;
import rps.server.command.annotations.CommandParam;
import rps.server.command.annotations.Commands;
import rps.server.mapper.CommandMapper;
import rps.server.network.ClientConnection;

@Service
@Setter
@RequiredArgsConstructor
@Slf4j
public class CommandManager implements InitializingBean {
  private final ApplicationContext context;
  private final Map<String, CommandExecutor> commandExecutors = new HashMap<>();
  private final MethodHandles.Lookup lookup = MethodHandles.lookup();
  private final CommandMapper commandMapper;

  @Override
  public void afterPropertiesSet() throws Exception {
    for (var commandHandler : context.getBeansWithAnnotation(CommandHandler.class).values()) {
      for (var method : commandHandler.getClass().getDeclaredMethods()) {
        if (method.isAnnotationPresent(Command.class)
            || method.isAnnotationPresent(Commands.class)) {
          registerCommandMethod(commandHandler, method);
        }
      }
    }
  }

  public void executeCommand(
      ClientConnection connection, String command, Map<String, Object> args) {
    if (!commandExecutors.containsKey(command)) {
      log.warn("Received unknown command {} from {}", command, connection.getConnectionId());
    }
    Optional.ofNullable(commandExecutors.get(command))
        .filter(e -> e.canExecute(connection))
        .ifPresentOrElse(
            e -> {
              var paramsMap = new ParameterMap(args);
              boolean success = true;
              try {
                success = e.execute(connection, command, paramsMap);
              } catch (Throwable ex) {
                success = false;
                log.error("Произошла ошибка при выполнении команды {}", command, ex);
                connection.sendPacket(
                    ServerResponse.error(
                        "Произошла ошибка при выполнении команды на стороне сервера"));
              } finally {
                if (success && e.isAnnounceCommandsOnSuccess()) {
                  broadcastAvailableCommands(connection);
                }
              }
            },
            () -> {
              if (connection.isLogged()) {
                broadcastAvailableCommands(connection);
              } else {
                connection.disconnect("Недопустимый ввод");
              }
            });
  }

  public void broadcastAvailableCommands(ClientConnection connection) {
    var builder =
        ServerCommands.builder()
            .availableCommands(
                commandExecutors.values().stream()
                    .filter(CommandExecutor::isAnnounceToClient)
                    .filter(c -> c.canExecute(connection))
                    .map(commandMapper::toCommandFormat)
                    .collect(Collectors.toList()));
    connection.sendPacket(builder.build());
  }

  public void registerCommandMethod(Object holder, Method method) throws IllegalAccessException {
    var returnType = method.getReturnType();
    Preconditions.checkState(
        returnType == void.class || returnType == boolean.class,
        method.getName() + " has invalid return type " + returnType);
    for (var annotation : method.getAnnotationsByType(Command.class)) {
      var executor =
          CommandExecutor.builder()
              .allowedStates(Arrays.stream(annotation.allowedStates()).collect(Collectors.toSet()))
              .description(annotation.description())
              .command(annotation.value())
              .method(method)
              .methodHandle(lookup.unreflect(method).bindTo(holder))
              .holder(holder)
              .announceCommandsOnSuccess(annotation.announceCommandsOnSuccess())
              .announceToClient(annotation.announceToClient())
              .category(annotation.category())
              .allowedParams(
                  Arrays.stream(annotation.params())
                      .collect(
                          Collectors.toMap(
                              CommandParam::value,
                              CommandParam::type,
                              (a, b) -> b,
                              LinkedHashMap::new)))
              .build();
      commandExecutors.put(executor.getCommand(), executor);
    }
  }
}
