package org.survy.domain.applicationservice.adapter.survey;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.survy.domain.applicationservice.dto.create.LogicAddRequest;
import org.survy.domain.applicationservice.dto.create.LogicAddResponse;
import org.survy.domain.applicationservice.mapper.LogicDataMapper;
import org.survy.domain.applicationservice.ports.output.cache.CacheService;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.domainService.SurveyDomainService;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.entity.logic.ParticipationLogic;
import org.survy.domain.core.valueObject.SurveyStatus;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class AddParticipationLogicToSurveyCommand extends SurveyUseCaseCommand {

    private final SurveyDomainService surveyDomainService;
    private final LogicDataMapper logicDataMapper;


    private static final String CACHE_KEY_PREFIX = "unconfiguredSurvey:";
    private static final int CACHE_TTL_IN_DAYS = 7;

    public AddParticipationLogicToSurveyCommand(SurveyDomainService surveyDomainService, SurveyRepository surveyRepository,
                                                LogicDataMapper logicDataMapper, CacheService<Survey> cacheService) {
        super(surveyRepository, cacheService);
        this.surveyDomainService = surveyDomainService;

        this.logicDataMapper = logicDataMapper;

    }

    @Transactional
    public LogicAddResponse addParticipationLogicToSurvey(LogicAddRequest request) {
        Survey survey = fetchSurvey(request.getSurveyId());
        ParticipationLogic logicToBeAdded = mapToDomainLogic(request);


        Survey logicAddedSurvey = surveyDomainService.addParticipationLogicToSurvey(logicToBeAdded, survey);

        saveSurvey(logicAddedSurvey);
        return logicDataMapper.participationLogicToLogicAddResponse(logicToBeAdded);
    }


    private ParticipationLogic mapToDomainLogic(LogicAddRequest request) {
        return logicDataMapper.logicAddRequestToParticipationLogic(request);
    }

    private Survey fetchSurvey(UUID surveyUUID) {
        String cacheKey = CACHE_KEY_PREFIX + surveyUUID;

        Survey survey = super.fetchFromCache(cacheKey);
        if (survey == null)
            survey = super.fetchSurveyFromDBWithAllAssociations(surveyUUID);

        return survey;
    }

    private void saveSurvey(Survey survey) {
        String cacheKey = CACHE_KEY_PREFIX + survey.getId().getValue().toString();


        if (survey.unconfigured())
            //TODO : make TimeUnit DAYS
            super.writeOnCache(cacheKey, survey, CACHE_TTL_IN_DAYS, TimeUnit.MINUTES);
        else
            super.updateSurveyInDatabase(survey);
    }
}
