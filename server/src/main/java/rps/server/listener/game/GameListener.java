package rps.server.listener.game;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.TaskScheduler;
import rps.server.network.GameServer;
import rps.server.properties.GamePlayProperties;import rps.server.service.GameService;
import rps.server.service.UserService;

@RequiredArgsConstructor
@Setter(onMethod = @__(@Autowired))
public abstract class GameListener<T extends ApplicationEvent> implements ApplicationListener<T> {
  protected GameServer gameServer;
  protected UserService userService;
  protected GameService gameService;
  protected TaskScheduler scheduler;
  protected GamePlayProperties gamePlayProperties;
}
