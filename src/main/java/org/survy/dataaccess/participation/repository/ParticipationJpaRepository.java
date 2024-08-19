package org.survy.dataaccess.participation.repository;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.survy.dataaccess.participation.entity.ParticipationEntity;
import org.survy.domain.core.entity.Participation;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParticipationJpaRepository extends JpaRepository<ParticipationEntity, UUID> {


    @Query("Select p from ParticipationEntity p " +
            "lEFT JOIN p.survey s " +
            "WHERE p.userId = :userId AND s.id = :surveyId")
    Optional<ParticipationEntity> findByUserIdAndSurveyId(@Param("userId") UUID userId, @Param("surveyId") UUID surveyId);

    @EntityGraph(attributePaths = "answers", type = EntityGraph.EntityGraphType.LOAD)
    Optional<ParticipationEntity> findWithAnswersById(UUID participationId);
}
