package rps.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rps.server.model.UserInfo;

@Getter
public class UserLoggedInEvent extends ApplicationEvent {
  private final UserInfo user;

  public UserLoggedInEvent(Object source, UserInfo user) {
    super(source);
    this.user = user;
  }
}
