package rps.server.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@AllArgsConstructor
public enum Item {
  ROCK("Камень"),
  PAPER("Бумага"),
  SCISSORS("Ножницы");

  private final String displayName;

  public static Item fromCommand(String name) {
    return Item.valueOf(name.toUpperCase());
  }

  public static Result getResult(Item item, Item that) {
    if (item == that) {
      return Result.DRAW;
    }
    var whatBeats = BEATS.get(item);
    return whatBeats == that ? Result.WIN : Result.LOSE;
  }

  public static Item getRandom() {
    var values = Item.values();
    return values[ThreadLocalRandom.current().nextInt(values.length)];
  }

  private static final Map<Item, Item> BEATS =
      new HashMap<>() {
        {
          put(ROCK, SCISSORS);
          put(PAPER, ROCK);
          put(SCISSORS, PAPER);
        }
      };
}
