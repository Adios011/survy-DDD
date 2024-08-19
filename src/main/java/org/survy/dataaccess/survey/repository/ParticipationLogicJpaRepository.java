package org.survy.dataaccess.survey.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;
import org.survy.dataaccess.survey.entity.ParticipationLogicEntity;
import org.survy.dataaccess.survey.entity.SurveyEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParticipationLogicJpaRepository extends JpaRepository<ParticipationLogicEntity , UUID> {

//    @EntityGraph(attributePaths = "survey" , type = EntityGraph.EntityGraphType.LOAD)
    List<ParticipationLogicEntity> findAllBySurvey( SurveyEntity survey);
}
