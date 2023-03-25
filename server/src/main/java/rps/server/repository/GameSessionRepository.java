package rps.server.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rps.server.entity.GameSessionEntity;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSessionEntity, Long> {
  Optional<GameSessionEntity> findFirstByUser_IdOrderByBeginTimeDesc(Long id);
  
}
