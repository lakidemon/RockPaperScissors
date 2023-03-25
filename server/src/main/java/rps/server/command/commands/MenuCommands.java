package rps.server.command.commands;

import lombok.RequiredArgsConstructor;
import rps.networking.packets.server.ServerMessage;
import rps.server.command.annotations.Command;
import rps.server.command.annotations.CommandHandler;
import rps.server.game.UserState;
import rps.server.model.UserInfo;
import rps.server.network.ClientConnection;
import rps.server.service.GameService;

@CommandHandler
@RequiredArgsConstructor
public class MenuCommands {
  private final GameService gameService;

  @Command(
      value = "start",
      category = "Меню",
      description = "Начать игру",
      announceCommandsOnSuccess = true,
      allowedStates = UserState.MENU)
  public void startGame(ClientConnection connection, UserInfo user) {
    connection.sendPacket(new ServerMessage("Игра начинается"));
    gameService.startGame(user);
  }
}
