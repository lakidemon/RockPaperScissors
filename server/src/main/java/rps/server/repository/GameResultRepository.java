package rps.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rps.server.entity.GameResultEntity;

@Repository
public interface GameResultRepository extends JpaRepository<GameResultEntity, Long> {
}