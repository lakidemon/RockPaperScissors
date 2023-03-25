package rps.server.listener.game;

import org.springframework.stereotype.Component;
import rps.networking.packets.server.ServerMessage;
import rps.server.event.GameRoundStartEvent;

@Component
public class GameRoundStartListener extends GameListener<GameRoundStartEvent> {

  @Override
  public void onApplicationEvent(GameRoundStartEvent event) {
    gameServer
        .getConnectionByUser(event.getUser())
        .sendPacket(new ServerMessage("Раунд #" + event.getRoundCount() + ". Ваш ход!"));
  }
}
