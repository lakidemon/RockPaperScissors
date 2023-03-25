package rps.server.listener;

import org.springframework.stereotype.Component;
import rps.networking.packets.server.ServerMessage;
import rps.server.event.GameRoundTickEvent;import rps.server.listener.game.GameListener;

@Component
public class RoundTimeleftNotifyListener extends GameListener<GameRoundTickEvent> {
  @Override
  public void onApplicationEvent(GameRoundTickEvent event) {
    var timeLeft = event.getRoundTick().timeLeft();
    if (gamePlayProperties.notifyChoiceTimeOn().contains(timeLeft)) {
      var user = event.getUser();
      gameServer
          .getConnectionByUser(user)
          .sendPacket(new ServerMessage("У вас осталось " + timeLeft + "сек"));
    }
  }
}
