package rps.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rps.server.model.GameResult;
import rps.server.model.UserInfo;

@Getter
public class GameEndEvent extends ApplicationEvent {
  private final UserInfo user;
  private final GameResult result;

  public GameEndEvent(Object source, UserInfo user, GameResult result) {
    super(source);
    this.user = user;
    this.result = result;
  }
}
