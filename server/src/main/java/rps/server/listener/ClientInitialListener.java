package rps.server.listener;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rps.server.network.GameServer;

@Component
@Order(0)
@RequiredArgsConstructor
public class ClientInitialListener {
  public static final long LOGIN_TIMEOUT = 5;
  private final GameServer gameServer;

  @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
  public void kickUnauthorized() {
    gameServer.getConnections().stream()
        .filter(c -> !c.isLogged())
        .filter(
            c -> Duration.between(c.getConnectedTime(), Instant.now()).toSeconds() >= LOGIN_TIMEOUT)
        .forEach(connection -> connection.disconnect("Превышено время ожидания"));
  }
}
