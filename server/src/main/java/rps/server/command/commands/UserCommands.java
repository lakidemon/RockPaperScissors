package rps.server.command.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import rps.server.command.annotations.Command;
import rps.server.command.annotations.CommandHandler;
import rps.server.command.annotations.CommandParam;
import rps.server.command.annotations.Input;
import rps.server.event.UserLoggedInEvent;
import rps.server.game.UserState;
import rps.server.mapper.UserMapper;
import rps.server.network.ClientConnection;
import rps.server.service.UserService;

@Slf4j
@CommandHandler
@RequiredArgsConstructor
public class UserCommands {
  private static final String AUTH_CATEGORY = "Авторизация";
  private final UserService userService;
  private final UserMapper userMapper;
  private final ApplicationEventPublisher eventPublisher;

  @Command(
      value = "signin",
      category = AUTH_CATEGORY,
      description = "Залогиниться",
      announceToClient = false,
      params = {@CommandParam("login"), @CommandParam("password")},
      announceCommandsOnSuccess = true)
  public boolean signIn(
      ClientConnection connection,
      @Input("login") String login,
      @Input("password") String password) {
    log.info("Login request from {}", login);
    var loginResult = userService.loginUser(login, password);
    connection.sendPacket(userMapper.toServerResponse(loginResult));

    if (loginResult.isSuccess()) {
      var user = loginResult.user();
      connection.setUser(user);
      eventPublisher.publishEvent(new UserLoggedInEvent(this, user));
    } else {
      connection.disconnect("Попробуйте ещё раз");
    }

    return loginResult.isSuccess();
  }

  @Command(
      value = "signup",
      category = AUTH_CATEGORY,
      description = "Зарегистрироваться",
      announceToClient = false,
      params = {@CommandParam("login"), @CommandParam("password")})
  public void signUp(
      ClientConnection connection,
      @Input("login") String login,
      @Input("password") String password) {
    log.info("Register request from {}", login);
    var registerResult = userService.registerUser(login, password);
    connection.sendPacket(userMapper.toServerResponse(registerResult));
    connection.disconnect("Соединение завершено");
  }

  @Command(
      value = "logout",
      category = "Общее",
      description = "Выйти из игры",
      allowedStates = {UserState.MENU, UserState.PLAYING, UserState.ENDGAME})
  public void logout(ClientConnection connection) {
    log.info("{} logged out", connection.getAddress());
    connection.disconnect("Вы вышли");
  }
}
