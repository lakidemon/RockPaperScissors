package rps.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rps.server.entity.GameSessionEntity;
import rps.server.model.UserInfo;

@Getter
public class UserHasUnfinishedGameEvent extends ApplicationEvent {
  private final UserInfo user;
  private final GameSessionEntity gameSession;

  public UserHasUnfinishedGameEvent(Object source, UserInfo user, GameSessionEntity gameSession) {
    super(source);
    this.user = user;
    this.gameSession = gameSession;
  }
}
