package org.survy.domain.applicationservice.adapter.survey;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.survy.domain.applicationservice.dto.create.SurveyCreationCompleteRequest;
import org.survy.domain.applicationservice.dto.create.SurveyCreationCompleteResponse;
import org.survy.domain.applicationservice.ports.output.cache.CacheService;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.domainService.SurveyDomainService;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.exception.SurveyDomainException;
import org.survy.domain.core.valueObject.SurveyId;

import java.util.UUID;

@Component
public class CompleteSurveyCreationCommand extends SurveyUseCaseCommand {

    private String cacheKey;
    private final String CACHE_KEY_PREFIX = "unconfiguredSurvey:";

    private final SurveyDomainService surveyDomainService;


    public CompleteSurveyCreationCommand(SurveyRepository surveyRepository, SurveyDomainService surveyDomainService, CacheService<Survey> cacheService) {
        super(surveyRepository, cacheService);
        this.surveyDomainService = surveyDomainService;
    }

    @Transactional
    public SurveyCreationCompleteResponse completeSurveyCreationProcess(SurveyCreationCompleteRequest request) {
        UUID surveyUUID = request.getSurveyId();
        cacheKey = CACHE_KEY_PREFIX + surveyUUID;

        Survey survey = fetchSurvey(surveyUUID);
        surveyDomainService.completeSurveyCreationProcess(survey);
        saveSurvey(survey);
        return new SurveyCreationCompleteResponse(surveyUUID, "Survey completed successfully");
    }


    private Survey fetchSurvey(UUID surveyUUID) {
        Survey survey = super.fetchFromCache(cacheKey);
        if (survey == null)
            survey = super.fetchFromDatabaseWithQuestions(surveyUUID);

        return survey;
    }

    private void saveSurvey(Survey survey) {
        super.updateSurveyWithAllAssociationsInDatabase(survey);
        deleteCache(cacheKey);
    }


}
