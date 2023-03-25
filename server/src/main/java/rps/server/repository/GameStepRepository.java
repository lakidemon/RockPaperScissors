package rps.server.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import rps.server.entity.GameStepEntity;

public interface GameStepRepository extends JpaRepositoryImplementation<GameStepEntity, Long> {
}