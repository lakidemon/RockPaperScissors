package rps.server.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import rps.networking.packets.server.ServerMessage;import rps.server.event.UserLoggedInEvent;
import rps.server.network.GameServer;
import rps.server.service.GameService;

@Component
@RequiredArgsConstructor
public class ReturnToTheGameListener implements ApplicationListener<UserLoggedInEvent> {
  private final GameService gameService;
  private final GameServer server;

  @Override
  public void onApplicationEvent(UserLoggedInEvent event) {
    var user = event.getUser();
    gameService.getLastUnfinishedGame(user).ifPresent(gameSession -> {
      server.getConnectionByUser(user).sendPacket(new ServerMessage("У вас есть незавершённая игра"));
      gameService.continueGame(user, gameSession);
    });
  }
}
