package org.survy.domain.applicationservice.adapter.participation;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.survy.domain.applicationservice.dto.cache.ParticipationCacheModel;
import org.survy.domain.applicationservice.ports.output.cache.CacheService;
import org.survy.domain.applicationservice.ports.output.repository.ParticipationRepository;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.domainService.ParticipationDomainService;
import org.survy.domain.core.entity.Participation;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.exception.ParticipationDomainException;
import org.survy.domain.core.exception.SurveyNotFoundException;
import org.survy.domain.core.valueObject.SurveyId;

import java.util.Optional;
import java.util.UUID;

@Component
public class CompleteParticipationCommand {

    private final ParticipationRepository participationRepository;
    private final ParticipationDomainService participationDomainService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ParticipationCommandHandler participationCommandHandler;
    private final CacheService<ParticipationCacheModel> cacheService;
    private final SurveyRepository surveyRepository;

    private static final String CACHE_KEY_PREFIX = "inProgressParticipation:";

    public CompleteParticipationCommand(ParticipationRepository participationRepository,
                                        ParticipationDomainService participationDomainService,
                                        ApplicationEventPublisher applicationEventPublisher,
                                        ParticipationCommandHandler participationCommandHandler,
                                        CacheService<ParticipationCacheModel> cacheService, SurveyRepository surveyRepository) {
        this.participationRepository = participationRepository;
        this.participationDomainService = participationDomainService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.participationCommandHandler = participationCommandHandler;
        this.cacheService = cacheService;
        this.surveyRepository = surveyRepository;
    }

    @Transactional
    public void completeParticipation(UUID participationId) {
        String cacheKey = CACHE_KEY_PREFIX + participationId;

        ParticipationCacheModel cacheModel = cacheService.get(cacheKey);
        Participation participation;
        if (cacheModel == null)
            participation = fetchAndCheckParticipation(participationId);
        else {
            Survey survey = fetchSurveyFromDBWithAllAssociations(cacheModel.getSurveyId());
            participation = merge(cacheModel , survey);
        }




        try {
            participationDomainService.completeParticipation(participation);
            updateParticipation(participation);
            cacheService.delete(cacheKey);
        } catch (ParticipationDomainException quotaEnabledException) {
            participationCommandHandler.updateParticipationStatus(participation.getId(), participation.getParticipationStatus());




            throw quotaEnabledException;
        }


    }

    private Participation fetchAndCheckParticipation(UUID participationId) {
        Optional<Participation> optional = participationRepository.findToAnswerQuestion(participationId);
        if (optional.isEmpty())
            throw new ParticipationDomainException("No such participation found " + participationId);

        return optional.get();
    }

    private void updateParticipation(Participation participation) {
        boolean isUpdated = participationRepository.update(participation);
        if (!isUpdated)
            throw new ParticipationDomainException("could not updated!");
    }

    private Survey fetchSurveyFromDBWithAllAssociations(SurveyId surveyId) {
        Optional<Survey> optional = surveyRepository.findWithQuestionsAndParticipationLogicsById(surveyId);
        if (optional.isEmpty())
            throw new SurveyNotFoundException(surveyId.getValue().toString());

        return optional.get();

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

}
