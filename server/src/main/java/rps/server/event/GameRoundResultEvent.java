package rps.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rps.server.model.GameRoundResult;
import rps.server.model.UserInfo;

@Getter
public class GameRoundResultEvent extends ApplicationEvent {
  private final UserInfo userInfo;
  private final GameRoundResult result;

  public GameRoundResultEvent(Object source, UserInfo userInfo, GameRoundResult result) {
    super(source);
    this.userInfo = userInfo;
    this.result = result;
  }
}
