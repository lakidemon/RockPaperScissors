package rps.server.model;

import java.util.UUID;
import lombok.Data;
import rps.server.game.UserState;

@Data
public final class UserInfo {
  private final String name;
  private final long id;
  private final UUID uuid;
  private Long currentSessionId;
  private UserState userState = UserState.MENU;
}
