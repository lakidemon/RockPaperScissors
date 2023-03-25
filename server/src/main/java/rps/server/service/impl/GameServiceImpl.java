package rps.server.service.impl;

import com.google.common.collect.Iterables;
import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rps.server.entity.GameResultEntity;
import rps.server.entity.GameSessionEntity;
import rps.server.entity.GameStepEntity;
import rps.server.event.GameEndEvent;
import rps.server.event.GameRoundResultEvent;
import rps.server.event.GameRoundStartEvent;
import rps.server.event.GameRoundTickEvent;
import rps.server.event.UserStateChangedEvent;
import rps.server.game.Item;
import rps.server.game.Result;
import rps.server.game.UserState;
import rps.server.model.GameResult;
import rps.server.model.GameRoundResult;
import rps.server.model.RoundTick;
import rps.server.model.UserInfo;
import rps.server.properties.GamePlayProperties;
import rps.server.repository.GameSessionRepository;
import rps.server.repository.GameStepRepository;
import rps.server.repository.UserRepository;
import rps.server.service.GameService;

@Service
@Transactional
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
  private final GameSessionRepository gameSessionRepository;
  private final GameStepRepository gameStepRepository;
  private final UserRepository userRepository;
  private final GamePlayProperties gamePlayProperties;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void startGame(UserInfo user) {
    var gameSession = new GameSessionEntity();
    gameSession.setUser(userRepository.getReferenceById(user.getId()));
    gameSessionRepository.save(gameSession);

    user.setCurrentSessionId(gameSession.getId());
    setUserState(user, UserState.PLAYING);

    startNewRound(user, gameSession);
  }

  @Override
  public void continueGame(UserInfo user, long gameSessionId) {
    var gameSession = gameSessionRepository.getReferenceById(gameSessionId);
    user.setCurrentSessionId(gameSession.getId());
    setUserState(user, UserState.PLAYING);

    eventPublisher.publishEvent(
        new GameRoundStartEvent(
            this, user, Iterables.getLast(gameSession.getGameSteps()).getCount()));
  }

  public void startNewRound(UserInfo user, GameSessionEntity gameSession) {
    var gameStep = new GameStepEntity();
    gameStep.setTimeLeft(gamePlayProperties.choiceTime());
    gameStep.setGameSession(gameSession);
    gameStep.setCount(gameSession.getGameSteps().size() + 1);

    gameSession.getGameSteps().add(gameStep);
    gameSessionRepository.save(gameSession);

    eventPublisher.publishEvent(new GameRoundStartEvent(this, user, gameStep.getCount()));
  }

  @Override
  public GameRoundResult makeChoice(UserInfo user, Item choice) {
    var gameSession = gameSessionRepository.getReferenceById(user.getCurrentSessionId());
    var gameStep = Iterables.getLast(gameSession.getGameSteps());
    gameStep.setComputerChoice(Item.getRandom());
    gameStep.setUserChoice(choice);
    return endRound(
        user, gameSession, gameStep, Item.getResult(choice, gameStep.getComputerChoice()));
  }

  @Override
  public RoundTick tickUser(UserInfo user) {
    var gameSession = gameSessionRepository.getReferenceById(user.getCurrentSessionId());
    var step = Iterables.getLast(gameSession.getGameSteps());
    var timeLeft = step.decrementTimeLeft();

    var roundTick = new RoundTick(timeLeft);
    eventPublisher.publishEvent(new GameRoundTickEvent(this, user, roundTick));

    if (timeLeft == 0) {
      endRound(user, gameSession, step, Result.LOSE);
    } else {
      gameStepRepository.save(step);
    }

    return roundTick;
  }

  public GameRoundResult endRound(
      UserInfo user, GameSessionEntity gameSession, GameStepEntity gameStep, Result result) {
    gameStep.setEndTime(OffsetDateTime.now());
    gameStep.setResult(result);
    gameStepRepository.save(gameStep);

    boolean newRound = gameStep.getCount() < gamePlayProperties.gameRounds();

    var roundResult =
        new GameRoundResult(
            gameStep.getResult(),
            gameStep.getComputerChoice(),
            gameStep.getTimeLeft() == 0,
            !newRound);

    eventPublisher.publishEvent(new GameRoundResultEvent(this, user, roundResult));

    if (newRound) {
      startNewRound(user, gameSession);
    } else {
      endGame(user);
    }

    return roundResult;
  }

  @Override
  public GameResult endGame(UserInfo user) {
    var gameSession = gameSessionRepository.getReferenceById(user.getCurrentSessionId());
    var gameResult = new GameResultEntity();
    gameResult.setResult(
        Result.getByScore(
            gameSession.getGameSteps().stream()
                .map(GameStepEntity::getResult)
                .mapToInt(Result::getScore)
                .sum()));
    gameSession.setEndTime(OffsetDateTime.now());
    gameSession.setGameResult(gameResult);

    gameSessionRepository.save(gameSession);

    var result = new GameResult(gameResult.getResult());
    eventPublisher.publishEvent(new GameEndEvent(this, user, result));
    setUserState(user, UserState.ENDGAME);
    return result;
  }

  @Override
  public Optional<Long> getLastUnfinishedGame(UserInfo user) {
    return gameSessionRepository
        .findFirstByUser_IdOrderByBeginTimeDesc(user.getId())
        .filter(g -> g.getGameResult() == null)
        .map(GameSessionEntity::getId);
  }

  @Override
  public void setUserState(UserInfo info, UserState state) {
    if (info.getUserState() != state) {
      info.setUserState(state);
      eventPublisher.publishEvent(new UserStateChangedEvent(this, info, state));
    }
  }
}
