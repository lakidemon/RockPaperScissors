package rps.server.model;

import java.util.Objects;

public record UserRegisterResult(UserInfo user, String message) {
  public boolean isSuccess() {
    return Objects.nonNull(user);
  }
}
