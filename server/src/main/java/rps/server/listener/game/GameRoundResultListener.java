package rps.server.listener.game;

import org.springframework.stereotype.Component;
import rps.networking.packets.server.ServerMessage;
import rps.server.event.GameRoundResultEvent;

@Component
public class GameRoundResultListener extends GameListener<GameRoundResultEvent> {

  @Override
  public void onApplicationEvent(GameRoundResultEvent event) {
    var user = event.getUserInfo();
    var gameRoundResult = event.getResult();
    var connection = gameServer.getConnectionByUser(user);
    if (gameRoundResult.timedOut()) {
      connection.sendPacket(new ServerMessage("Время на ответ истекло"));
    } else {
      connection.sendPacket(
          new ServerMessage(
              "Выбор противника: " + gameRoundResult.opponentChoice().getDisplayName()));
    }
    connection.sendPacket(
        new ServerMessage("Результат - " + gameRoundResult.roundResult().getDisplayName()));
  }
}
