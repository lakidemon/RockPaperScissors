package rps.server.listener.game;

import org.springframework.stereotype.Component;
import rps.networking.packets.server.ServerMessage;
import rps.server.event.GameEndEvent;
import rps.server.game.UserState;
import rps.server.model.UserInfo;
import rps.server.network.ClientConnection;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class GameEndListener extends GameListener<GameEndEvent> {

  @Override
  public void onApplicationEvent(GameEndEvent event) {
    var user = event.getUser();
    var result = event.getResult();
    var connection = gameServer.getConnectionByUser(user);
    connection.sendPacket(
        new ServerMessage("Игра окончена! Результат - " + result.result().getDisplayName()));
    scheduler.schedule(
        () -> returnToMenu(connection, user), Instant.now().plus(5, ChronoUnit.SECONDS));
  }

  public void returnToMenu(ClientConnection connection, UserInfo userInfo) {
    if (connection.isConnected()) {
      gameService.setUserState(userInfo, UserState.MENU);
    }
  }
}
