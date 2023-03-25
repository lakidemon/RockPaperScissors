package rps.server.command.commands;

import lombok.RequiredArgsConstructor;
import rps.networking.packets.server.ServerMessage;
import rps.server.command.annotations.Command;
import rps.server.command.annotations.CommandHandler;
import rps.server.command.annotations.Commands;
import rps.server.game.Item;
import rps.server.game.UserState;
import rps.server.model.UserInfo;
import rps.server.network.ClientConnection;
import rps.server.service.GameService;

@CommandHandler
@RequiredArgsConstructor
public class GameCommands {
  private static final String CATEGORY = "Игра";
  private final GameService gameService;

  @Commands({
    @Command(
        value = "rock",
        category = CATEGORY,
        description = "Выбрать камень",
        allowedStates = UserState.PLAYING),
    @Command(
        value = "paper",
        category = CATEGORY,
        description = "Выбрать бумагу",
        allowedStates = UserState.PLAYING),
    @Command(
        value = "scissors",
        category = CATEGORY,
        description = "Выбрать ножницы",
        allowedStates = UserState.PLAYING)
  })
  public void makeChoice(ClientConnection connection, UserInfo user, String choice) {
    Item item = Item.fromCommand(choice);
    connection.sendPacket(new ServerMessage("Ваш выбор: " + item.getDisplayName()));
    gameService.makeChoice(user, item);
  }
  
}
