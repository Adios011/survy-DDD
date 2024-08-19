package org.survy.dataaccess.participation.adapter;

import jakarta.persistence.*;
import org.hibernate.graph.SubGraph;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.survy.dataaccess.participation.entity.ParticipationEntity;
import org.survy.dataaccess.participation.mapper.ParticipationDataAccessMapper;
import org.survy.dataaccess.participation.repository.ParticipationJpaRepository;
import org.survy.dataaccess.survey.entity.ParticipationLogicEntity;
import org.survy.dataaccess.survey.entity.SurveyEntity;
import org.survy.dataaccess.survey.repository.ParticipationLogicJpaRepository;
import org.survy.dataaccess.survey.repository.SurveyJpaRepository;
import org.survy.domain.applicationservice.ports.output.repository.ParticipationRepository;
import org.survy.domain.core.entity.Participation;
import org.survy.domain.core.valueObject.Option;
import org.survy.domain.core.valueObject.ParticipationId;
import org.survy.domain.core.valueObject.ParticipationStatus;

import java.util.*;

@Repository("participationRepository")
public class ParticipationRepositoryImpl implements ParticipationRepository {

    private final ParticipationJpaRepository participationJpaRepository;
    private final SurveyJpaRepository surveyJpaRepository;
    private final ParticipationLogicJpaRepository participationLogicJpaRepository;
    private final ParticipationDataAccessMapper participationDataAccessMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public ParticipationRepositoryImpl(ParticipationJpaRepository participationJpaRepository,
                                       SurveyJpaRepository surveyJpaRepository,
                                       ParticipationLogicJpaRepository participationLogicJpaRepository,
                                       ParticipationDataAccessMapper participationDataAccessMapper) {
        this.participationJpaRepository = participationJpaRepository;
        this.surveyJpaRepository = surveyJpaRepository;
        this.participationLogicJpaRepository = participationLogicJpaRepository;


        this.participationDataAccessMapper = participationDataAccessMapper;
    }

    @Override
    public boolean save(Participation participation) {
        if (participation == null) return false;

        ParticipationEntity participationEntity = participationDataAccessMapper.toEntity(participation);
        participationJpaRepository.save(participationEntity);
        return true;
    }

    @Override
    @Transactional
    public boolean update(Participation participation) {

        System.out.println("***********THE BEGINNING OF UPDATE METHOD ************");
        ParticipationEntity jpaEntity = entityManager.find(ParticipationEntity.class, participation.getId().getValue());

        participationDataAccessMapper.copyFromDomain(participation, jpaEntity);

        entityManager.merge(jpaEntity);


        return true;

    }

    @Override
    public Optional<Participation> findByUserIdAndSurveyId(UUID userId, UUID surveyId) {
        Optional<ParticipationEntity> optional = participationJpaRepository.findByUserIdAndSurveyId(userId, surveyId);
        if (optional.isEmpty())
            return Optional.empty();

        return Optional.of(participationDataAccessMapper.toParticipation(optional.get()));


    }


    @Override
    @Transactional
    public boolean updateStatus(ParticipationId id, ParticipationStatus newStatus) {
        Optional<ParticipationEntity> optional = participationJpaRepository.findById(id.getValue());
        if (optional.isEmpty()) return false;
        ParticipationEntity jpaEntity = optional.get();
        jpaEntity.setParticipationStatus(newStatus);

        ParticipationEntity updated = participationJpaRepository.save(jpaEntity);

        return updated.getParticipationStatus().equals(newStatus);

    }

    @Override
    @Transactional
    public Optional<Participation> findToAnswerQuestion(UUID participationId) {
//        System.out.println("*************************************FINDTOANSWER********************");

//        Optional<ParticipationEntity> optionalParticipation = participationJpaRepository.findWithAnswersById(participationId);
//        if (optionalParticipation.isEmpty()) return Optional.empty();

        System.out.println("WHİLE GETTING SURVEY IDD");
        System.out.println("WHİLE GETTING SURVEY IDD");
//        ParticipationEntity jpaParticipation = optionalParticipation.get();
//        UUID surveyUUID = jpaParticipation.getSurvey().getId();
        System.out.println("WHİLE GETTING SURVEY IDD");
        System.out.println("WHİLE GETTING SURVEY IDD");

        // load survey with questions
//        Optional<SurveyEntity> optionalSurvey = surveyJpaRepository.findWithQuestionsById(surveyUUID);
//        SurveyEntity jpaSurvey = optionalSurvey.get();
        // load participationLogics separately, to avoid cartesian product.
//        List<ParticipationLogicEntity> jpaParticipationLogics = participationLogicJpaRepository.findAllBySurvey(jpaSurvey);
//        jpaSurvey.setParticipationLogics(jpaParticipationLogics);

        //Set survey to participation
//        jpaParticipation.setSurvey(jpaSurvey);

        EntityGraph<ParticipationEntity> entityGraph = entityManager.createEntityGraph(ParticipationEntity.class);
        entityGraph.addAttributeNodes("survey", "answers");
        Subgraph<SurveyEntity> surveyEntitySubGraph = entityGraph.addSubgraph("survey", SurveyEntity.class);
        surveyEntitySubGraph.addAttributeNodes("questions", "participationLogics");
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", entityGraph);

        ParticipationEntity jpaParticipation = entityManager.find(ParticipationEntity.class, participationId, hints);
        if (jpaParticipation == null)
            return Optional.empty();
        //TODO : convert jpa entity to domain entity
        System.out.println("*************************************FINDTOANSWER********************");
        return Optional.of(participationDataAccessMapper.toParticipationWithSurveyAndAnswers(jpaParticipation));


    }


}
