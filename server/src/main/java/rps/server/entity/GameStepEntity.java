package rps.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import rps.server.game.Item;
import rps.server.game.Result;

@Getter
@Setter
@Entity
@Table(name = "game_steps")
public class GameStepEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  private int count;
  private Item userChoice;
  private Item computerChoice;
  private Result result;
  private int timeLeft;
  private OffsetDateTime beginTime = OffsetDateTime.now();
  private OffsetDateTime endTime;

  @ManyToOne
  @JoinColumn(name = "game_session_id")
  private GameSessionEntity gameSession;

  public int decrementTimeLeft() {
    return timeLeft = timeLeft - 1;
  }
}
