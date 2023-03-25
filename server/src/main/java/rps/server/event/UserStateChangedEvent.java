package rps.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rps.server.game.UserState;
import rps.server.model.UserInfo;

@Getter
public class UserStateChangedEvent extends ApplicationEvent {
  private final UserInfo userInfo;
  private final UserState newState;

  public UserStateChangedEvent(Object source, UserInfo userInfo, UserState newState) {
    super(source);
    this.userInfo = userInfo;
    this.newState = newState;
  }
}
