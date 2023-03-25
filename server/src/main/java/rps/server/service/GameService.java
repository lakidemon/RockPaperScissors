package rps.server.service;

import java.util.Optional;
import rps.server.game.Item;
import rps.server.game.UserState;
import rps.server.model.GameResult;
import rps.server.model.GameRoundResult;
import rps.server.model.RoundTick;
import rps.server.model.UserInfo;

public interface GameService {

  void startGame(UserInfo user);

  void continueGame(UserInfo user, long gameSessionId);

  GameRoundResult makeChoice(UserInfo user, Item choice);

  RoundTick tickUser(UserInfo info);

  GameResult endGame(UserInfo user);

  Optional<Long> getLastUnfinishedGame(UserInfo user);

  void setUserState(UserInfo user, UserState newState);
}
