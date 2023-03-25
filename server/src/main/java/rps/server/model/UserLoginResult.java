package rps.server.model;

import java.util.Objects;

public record UserLoginResult(UserInfo user, String message) {

  public boolean isSuccess() {
    return Objects.nonNull(user);
  }
}
