package rps.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rps.server.model.UserInfo;

@Getter
public class GameRoundStartEvent extends ApplicationEvent {
  private final UserInfo user;
  private final int roundCount;

  public GameRoundStartEvent(Object source, UserInfo user, int roundCount) {
    super(source);
    this.user = user;
    this.roundCount = roundCount;
  }
}
