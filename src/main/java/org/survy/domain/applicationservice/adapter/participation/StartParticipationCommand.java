package org.survy.domain.applicationservice.adapter.participation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.survy.domain.applicationservice.dto.cache.ParticipationCacheModel;
import org.survy.domain.applicationservice.dto.participation.ParticipationStartRequest;
import org.survy.domain.applicationservice.dto.participation.ParticipationStartResponse;
import org.survy.domain.applicationservice.mapper.ParticipationDataMapper;
import org.survy.domain.applicationservice.ports.output.cache.CacheService;
import org.survy.domain.applicationservice.ports.output.repository.ParticipationRepository;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.applicationservice.ports.output.repository.UserRepository;
import org.survy.domain.core.domainService.ParticipationDomainService;
import org.survy.domain.core.entity.Participation;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.event.ParticipationCancelledEvent;
import org.survy.domain.core.exception.*;
import org.survy.domain.core.valueObject.UserId;

import java.util.UUID;

@Slf4j
@Component
public class StartParticipationCommand extends ParticipationUseCaseCommand {


    private static final String ANSWER_CACHE_KEY_PREFIX = "inProgressAnswers:";
    private static final int CACHE_TTL_IN_DAYS = 5;

    private final ParticipationDomainService participationDomainService;
    private final ParticipationDataMapper participationDataMapper;
    private final UserRepository userRepository;
    private final ParticipationCommandHandler participationCommandHandler;
    private final CacheService<ParticipationCacheModel> cacheService;
    private final ApplicationEventPublisher applicationEventPublisher;


    public StartParticipationCommand(ParticipationRepository participationRepository,
                                     SurveyRepository surveyRepository, ParticipationDomainService participationDomainService,
                                     ParticipationDataMapper participationDataMapper, UserRepository userRepository, ParticipationCommandHandler participationCommandHandler, CacheService<ParticipationCacheModel> cacheService, ApplicationEventPublisher applicationEventPublisher) {
        super(participationRepository, surveyRepository);

        this.participationDomainService = participationDomainService;
        this.participationDataMapper = participationDataMapper;

        this.userRepository = userRepository;

        this.participationCommandHandler = participationCommandHandler;
        this.cacheService = cacheService;

        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Transactional
    public ParticipationStartResponse startParticipationProcess(ParticipationStartRequest request) throws ParticipationDomainException {
        UUID userId = request.getUserId();
        UUID surveyId = request.getSurveyId();

        checkParticipant(userId);
        Participation participation = fetchParticipation(userId, surveyId);


        if (participation == null)
            participation = startAndSaveNewParticipationProcess(request);
        else if (participation.completed() || participation.canceled())
            throw new ParticipationDomainException("has been already participated!");
        else
            handleExistingParticipationProcess(participation, request);


        return participationDataMapper.toParticipationStartResponse(participation);
    }

    private Participation startAndSaveNewParticipationProcess(ParticipationStartRequest request) {
        Survey survey = fetchSurveyFromDBWithAllAssociations(request.getSurveyId());

        Participation newParticipation;

        newParticipation = participationDomainService.startNewParticipationProcess(new UserId(request.getUserId()), survey);
        saveNewParticipation(newParticipation);


        return newParticipation;
    }

    private void handleExistingParticipationProcess(Participation existingParticipation, ParticipationStartRequest request) {
        UUID participationUUID = existingParticipation.getId().getValue();
        String cacheKey = ANSWER_CACHE_KEY_PREFIX + participationUUID;

        ParticipationCacheModel cacheModel = cacheService.get(cacheKey);
        if (cacheModel != null)
            existingParticipation = mapFromCacheModel(cacheModel);

        Survey survey = fetchSurveyFromDBWithAllAssociations(request.getSurveyId());


        try {
            participationDomainService.handleExistingParticipation(existingParticipation, new UserId(request.getUserId()), survey);
        } catch (ParticipationDomainException exception) {
//            participationCommandHandler.updateParticipationStatus(existingParticipation.getId(), existingParticipation.getParticipationStatus());
            applicationEventPublisher.publishEvent(new ParticipationCancelledEvent(existingParticipation.getId(), exception.getMessage()));
            throw exception;
        }


    }

    private Participation mapFromCacheModel(ParticipationCacheModel cacheModel) {
        return Participation.builder()
                .participationId(cacheModel.getParticipationId())
                .answers(cacheModel.getAnswers())
                .participationStatus(cacheModel.getParticipationStatus())
                .answeredQuestionIds(cacheModel.getAnsweredQuestionIds())
                .userId(cacheModel.getUserId())
                .build();
    }


    private void checkParticipant(UUID userId) {
        if (!userRepository.existsByUserId(userId))
            throw new UserNotFoundException("No such user with provided setId " + userId);
    }

    private Participation fetchParticipation(UUID userId, UUID surveyId) {
        return participationRepository.findByUserIdAndSurveyId(userId, surveyId).orElse(null);

    }


    protected void saveNewParticipation(Participation newParticipation) {
        boolean isSaved = participationRepository.save(newParticipation);
        if (!isSaved) throw new ParticipationDomainException("could not saved!");

    }


}
