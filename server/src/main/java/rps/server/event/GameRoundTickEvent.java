package rps.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rps.server.model.RoundTick;
import rps.server.model.UserInfo;

@Getter
public class GameRoundTickEvent extends ApplicationEvent {
  private final UserInfo user;
  private final RoundTick roundTick;

  public GameRoundTickEvent(Object source, UserInfo user, RoundTick roundTick) {
    super(source);
    this.roundTick = roundTick;
    this.user = user;
  }
}
