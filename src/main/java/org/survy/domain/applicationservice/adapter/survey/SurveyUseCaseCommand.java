package org.survy.domain.applicationservice.adapter.survey;

import org.survy.domain.applicationservice.adapter.UseCaseCommand;
import org.survy.domain.applicationservice.ports.output.cache.CacheService;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.exception.SurveyDomainException;
import org.survy.domain.core.exception.SurveyNotFoundException;
import org.survy.domain.core.valueObject.SurveyId;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class SurveyUseCaseCommand extends UseCaseCommand {


    private final CacheService<Survey> cacheService;

    public SurveyUseCaseCommand(SurveyRepository surveyRepository, CacheService<Survey> cacheService) {
        super(surveyRepository);
        this.cacheService = cacheService;
    }

    protected void updateSurveyInDatabase(Survey survey) {
        boolean isUpdated = surveyRepository.update(survey);
        if (!isUpdated)
            throw new SurveyDomainException("Could not updated!");
    }

    protected void updateSurveyWithAllAssociationsInDatabase(Survey survey) {
        boolean isUpdated = surveyRepository.updateWithAllAssociations(survey);
        if (!isUpdated)
            throw new SurveyDomainException("could not updated!");
    }

    protected void deleteCache(String key) {
        cacheService.delete(key);
    }

    protected void writeOnCache(String cacheKey, Survey survey, long timeout, TimeUnit timeUnit) {
        cacheService.save(cacheKey, survey, timeout, TimeUnit.MINUTES);
    }

    protected Survey fetchFromCache(String cacheKey) {
        return cacheService.get(cacheKey);
    }



    protected Survey fetchFromDatabaseWithQuestions(UUID surveyUUID) {
        Optional<Survey> optional = surveyRepository.findWithQuestionsById(new SurveyId(surveyUUID));
        if (optional.isEmpty())
            throw new SurveyNotFoundException(surveyUUID.toString());

        return optional.get();
    }


    public SurveyRepository getSurveyRepository() {
        return surveyRepository;
    }

    public CacheService<Survey> getCacheService() {
        return cacheService;
    }
}
