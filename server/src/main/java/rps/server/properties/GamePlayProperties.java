package rps.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(value = "game.game-play")
public record GamePlayProperties(int choiceTime, List<Integer> notifyChoiceTimeOn, int gameRounds) {}
