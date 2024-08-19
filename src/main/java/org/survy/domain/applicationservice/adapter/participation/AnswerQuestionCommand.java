package org.survy.domain.applicationservice.adapter.participation;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.survy.domain.applicationservice.dto.cache.ParticipationCacheModel;
import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.applicationservice.dto.participation.AnswerResponse;
import org.survy.domain.applicationservice.ports.output.cache.CacheService;
import org.survy.domain.applicationservice.ports.output.repository.ParticipationRepository;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.domainService.ParticipationDomainService;
import org.survy.domain.core.entity.Participation;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.exception.ParticipationDomainException;
import org.survy.domain.core.valueObject.SurveyId;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
public class AnswerQuestionCommand {

    private static final String CACHE_KEY_PREFIX = "inProgressParticipation:";


    private final ParticipationRepository participationRepository;
    private final SurveyRepository surveyRepository;
    private final ParticipationDomainService participationDomainService;
    private final ParticipationCommandHandler participationCommandHandler;
    private final CacheService<ParticipationCacheModel> cacheService;

    public AnswerQuestionCommand(ParticipationRepository participationRepository, SurveyRepository surveyRepository, ParticipationDomainService participationDomainService, ParticipationCommandHandler participationCommandHandler, CacheService<ParticipationCacheModel> cacheService) {
        this.participationRepository = participationRepository;
        this.surveyRepository = surveyRepository;
        this.participationDomainService = participationDomainService;
        this.participationCommandHandler = participationCommandHandler;
        this.cacheService = cacheService;
    }


//    @Transactional
//    public AnswerResponse answerQuestion(AnswerRequest request) {
//
//        Participation
//                participation = fetchParticipationToAnswerQuestion(request.getParticipationId(), request.getSurveyId());
//
//
//
//        Participation participationQuestionAnswered;
//        try {
//            participationQuestionAnswered = participationDomainService.answerQuestion(participation, request);
//            updateParticipation(participationQuestionAnswered);
//        } catch (ParticipationDomainException participationDomainException) {
//            participationCommandHandler.updateParticipationStatus(participation.getId(), participation.getParticipationStatus());
//            throw participationDomainException;
//        }
//
//
//        return new AnswerResponse("Question has been answered successfully", request);
//    }


    @Transactional
    public AnswerResponse answerQuestion(AnswerRequest request) {

        UUID participationId = request.getParticipationId();
        UUID surveyId = request.getSurveyId();
        String cacheKey = CACHE_KEY_PREFIX + participationId;
        ParticipationCacheModel participationCacheModel = cacheService.get(cacheKey);
        System.out.println(participationCacheModel);

        Participation participation;
        Survey survey;
        if (participationCacheModel == null)
            participation = fetchParticipationToAnswerQuestion(participationId, surveyId);
        else {
            survey = fetchSurveyFromDBWithAllAssociations(surveyId);
            participation = merge(participationCacheModel, survey);
        }




        Participation participationQuestionAnswered;
        try {
            participationQuestionAnswered = participationDomainService.answerQuestion(participation, request);
//            updateParticipation(participationQuestionAnswered);
            cacheService.save(cacheKey , toCacheModel(participationQuestionAnswered) , 10 , TimeUnit.MINUTES);
        } catch (ParticipationDomainException participationDomainException) {
            participationCommandHandler.updateParticipationStatus(participation.getId(), participation.getParticipationStatus());
            throw participationDomainException;
        }


        return new AnswerResponse("Question has been answered successfully", request);
    }


    private Participation fetchParticipationToAnswerQuestion(UUID participationId, UUID surveyId) {
        System.out.println("*********MAPLERKENNN*****");
        Optional<Participation> optional = participationRepository.findToAnswerQuestion(participationId);
        System.out.println("*********MAPLERKENNN*****");

        if (optional.isEmpty())
            throw new ParticipationDomainException("No such participation found " + participationId);


        return optional.get();
    }

    private void updateParticipation(Participation participation) {
        boolean isUpdated = participationRepository.update(participation);
        if (!isUpdated)
            throw new ParticipationDomainException("could not updated!");
    }


    private Participation merge(ParticipationCacheModel cacheModel, Survey survey) {
        if (!cacheModel.getSurveyId().equals(survey.getId()))
            throw new ParticipationDomainException("No such participation found!");
        return Participation.builder()
                .participationId(cacheModel.getParticipationId())
                .participationStatus(cacheModel.getParticipationStatus())
                .answeredQuestionIds(cacheModel.getAnsweredQuestionIds())
                .userId(cacheModel.getUserId())
                .answers(cacheModel.getAnswers())
                .survey(survey)
                .build();
    }

    private ParticipationCacheModel toCacheModel(Participation participation){
        ParticipationCacheModel participationCacheModel = new ParticipationCacheModel();
        participationCacheModel.setUserId(participation.getUserId());
        participationCacheModel.setAnswers(participation.getAnswers());
        participationCacheModel.setAnsweredQuestionIds(participation.getAnsweredQuestionIds());
        participationCacheModel.setSurveyId(participation.getSurvey().getId());
        participationCacheModel.setParticipationStatus(participation.getParticipationStatus());
        participationCacheModel.setParticipationId(participation.getId());
        return participationCacheModel;

    }


    private Survey fetchSurveyFromDBWithAllAssociations(UUID surveyId) {
        Optional<Survey> optional = surveyRepository.findWithQuestionsAndParticipationLogicsById(new SurveyId(surveyId));
        if (optional.isEmpty())
            throw new ParticipationDomainException("no such survey found " + surveyId);

        return optional.get();
    }


}
