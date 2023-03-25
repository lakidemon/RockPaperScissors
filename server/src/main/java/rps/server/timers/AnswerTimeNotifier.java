package rps.server.timers;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rps.server.game.UserState;
import rps.server.network.ClientConnection;
import rps.server.network.GameServer;
import rps.server.service.GameService;

@Component
@RequiredArgsConstructor
public class AnswerTimeNotifier {
  private final GameServer gameServer;
  private final GameService gameService;

  @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
  public void announceTime() {
    gameServer.getConnections().stream()
        .filter(ClientConnection::isLogged)
        .map(ClientConnection::getUser)
        .filter(u -> u.getUserState() == UserState.PLAYING)
        .forEach(gameService::tickUser);
  }
}
