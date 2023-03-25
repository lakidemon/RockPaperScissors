package rps.server.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Result {
  WIN("Победа", 1),
  LOSE("Поражение", -1),
  DRAW("Ничья", 0);

  private final String displayName;
  private final int score;

  public static Result getByScore(int score) {
    if (score == 0) {
      return DRAW;
    } else if (score > 0) {
      return WIN;
    } else {
      return LOSE;
    }
  }
}
