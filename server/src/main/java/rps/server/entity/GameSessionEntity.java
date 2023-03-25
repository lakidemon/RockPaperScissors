package rps.server.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "game_sessions")
public class GameSessionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  private UserEntity user;

  @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL)
  private List<GameStepEntity> gameSteps = new ArrayList<>();

  @OneToOne(cascade = CascadeType.ALL)
  private GameResultEntity gameResult;

  private OffsetDateTime beginTime = OffsetDateTime.now();
  private OffsetDateTime endTime;
}