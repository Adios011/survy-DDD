package org.survy.dataaccess.survey.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.survy.dataaccess.survey.entity.SurveyEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SurveyJpaRepository extends JpaRepository<SurveyEntity, UUID> {

    @EntityGraph(attributePaths = {"questions" } , type = EntityGraph.EntityGraphType.LOAD)
    Optional<SurveyEntity> findWithQuestionsById(UUID id);
}
