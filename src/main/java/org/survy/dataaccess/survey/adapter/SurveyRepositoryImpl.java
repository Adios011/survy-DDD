package org.survy.dataaccess.survey.adapter;


import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.survy.dataaccess.survey.entity.SurveyEntity;
import org.survy.dataaccess.survey.mapper.SurveyDataAccessMapper;
import org.survy.dataaccess.survey.repository.ParticipationLogicJpaRepository;
import org.survy.dataaccess.survey.repository.SurveyJpaRepository;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.valueObject.SurveyId;
import org.survy.domain.core.valueObject.SurveyStatus;

import java.util.*;
import java.util.Optional;

@Repository("surveyRepository")
public class SurveyRepositoryImpl implements SurveyRepository {

    @PersistenceContext
    private EntityManager entityManager;


    private final SurveyJpaRepository surveyJpaRepository;
    private final SurveyDataAccessMapper surveyDataAccessMapper;


    public SurveyRepositoryImpl(SurveyJpaRepository surveyJpaRepository, SurveyDataAccessMapper surveyDataAccessMapper) {
        this.surveyJpaRepository = surveyJpaRepository;
        this.surveyDataAccessMapper = surveyDataAccessMapper;

    }

    @Override
    @Transactional
    public boolean save(Survey survey) {
        if (survey == null) return false;

        SurveyEntity surveyEntityToBeSaved = surveyDataAccessMapper.toEntity(survey);
        entityManager.persist(surveyEntityToBeSaved);
        return true;
    }


    @Override
    @Transactional
    public boolean update(Survey survey) {
        SurveyEntity jpaEntity = entityManager.find(SurveyEntity.class, survey.getId().getValue());
        if (jpaEntity == null) return false;

        surveyDataAccessMapper.copyFromDomain(jpaEntity, survey);
        entityManager.merge(jpaEntity);
        return true;
    }

    @Override
    @Transactional
    public boolean updateWithAllAssociations(Survey survey) {
        String graphName = "survey.questionsAndParticipationLogics";
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(graphName);
        if (entityGraph == null)
            throw new RuntimeException("No such entity graph: " + graphName);
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", entityGraph);
        SurveyEntity jpaEntity = entityManager.find(SurveyEntity.class, survey.getId().getValue(), hints);

        surveyDataAccessMapper.copyFromDomain(jpaEntity, survey);

        entityManager.merge(jpaEntity);

        return true;

    }

    @Override
    @Transactional
    public boolean updateStatus(SurveyId surveyId, SurveyStatus newStatus, String reason) {
        Optional<SurveyEntity> optional = surveyJpaRepository.findById(surveyId.getValue());
        if (optional.isEmpty()) return false;

        SurveyEntity jpaEntity = optional.get();
        jpaEntity.setSurveyStatus(newStatus);
        jpaEntity.setClosureReason(reason);

        SurveyEntity updated = surveyJpaRepository.save(jpaEntity);
        System.err.println(updated);

        return updated.getSurveyStatus().equals(newStatus);
    }

    @Override
    @Transactional
    public Optional<Survey> findById(SurveyId surveyId) {
        return Optional.empty();
    }

    @Override
    public Optional<Survey> findWithQuestionsById(SurveyId surveyId) {
        EntityGraph<SurveyEntity> entityGraph = entityManager.createEntityGraph(SurveyEntity.class);
        entityGraph.addAttributeNodes("questions");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", entityGraph);
        SurveyEntity jpaEntity = entityManager.find(SurveyEntity.class, surveyId.getValue(), hints);

        if (jpaEntity == null)
            return Optional.empty();

        return Optional.of(surveyDataAccessMapper.toSurveyWithQuestions(jpaEntity));

    }

    @Override
    @Transactional
    public Optional<Survey> findWithParticipationLogicsById(SurveyId surveyId) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Survey> findWithQuestionsAndParticipationLogicsById(SurveyId surveyId) {
        String graphName = "survey.questionsAndParticipationLogics";
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(graphName);
        if (entityGraph == null)
            throw new RuntimeException("No such entity graph: " + graphName);
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", entityGraph);
        SurveyEntity jpaEntity = entityManager.find(SurveyEntity.class, surveyId.getValue(), hints);

        if (jpaEntity == null) return Optional.empty();


        return Optional.of(surveyDataAccessMapper.toSurveyWithAllAssociations(jpaEntity));


    }
}
