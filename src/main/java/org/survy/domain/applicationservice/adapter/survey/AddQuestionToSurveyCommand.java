package org.survy.domain.applicationservice.adapter.survey;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.survy.domain.applicationservice.dto.create.QuestionAddRequest;
import org.survy.domain.applicationservice.dto.create.QuestionAddResponse;
import org.survy.domain.applicationservice.mapper.QuestionDataMapper;
import org.survy.domain.applicationservice.ports.output.cache.CacheService;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.domainService.SurveyDomainService;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.entity.question.Question;
import org.survy.domain.core.exception.SurveyNotFoundException;
import org.survy.domain.core.valueObject.SurveyId;
import org.survy.domain.core.valueObject.SurveyStatus;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class AddQuestionToSurveyCommand extends SurveyUseCaseCommand {

    private final SurveyRepository surveyRepository;
    private final SurveyDomainService surveyDomainService;
    private final QuestionDataMapper questionDataMapper;
    private final SurveyCommandHandler surveyCommandHandler;


    private static final String CACHE_KEY_PREFIX = "unconfiguredSurvey:";
    private static final int CACHE_TTL_IN_DAYS = 7;


    public AddQuestionToSurveyCommand(SurveyRepository surveyRepository,
                                      SurveyDomainService surveyDomainService,
                                      QuestionDataMapper questionDataMapper,
                                      SurveyCommandHandler surveyCommandHandler,
                                      CacheService<Survey> cacheService) {
        super(surveyRepository, cacheService);
        this.surveyRepository = surveyRepository;
        this.surveyDomainService = surveyDomainService;
        this.questionDataMapper = questionDataMapper;
        this.surveyCommandHandler = surveyCommandHandler;
    }


    @Transactional
    public QuestionAddResponse addQuestionToSurvey(QuestionAddRequest request) {

        Survey survey = fetchSurvey(request.getSurveyId());
        Question questionToBeAdded = mapToQuestionDomain(request);

        Survey questionAddedSurvey = surveyDomainService.addQuestionToSurvey(questionToBeAdded, survey);

        saveSurvey(questionAddedSurvey);
        return questionDataMapper.questionToQuestionAddResponse(questionToBeAdded);

    }


    private Question mapToQuestionDomain(QuestionAddRequest request) {
        return questionDataMapper.questionAddRequestToQuestion(request);
    }


    private Survey fetchSurvey(UUID surveyUUID) {
        String cacheKey = CACHE_KEY_PREFIX + surveyUUID;

        Survey survey = super.fetchFromCache(cacheKey);
        if (survey == null)
            survey = super.fetchFromDatabaseWithQuestions(surveyUUID);

        return survey;
    }

    private void saveSurvey(Survey survey) {
        String cacheKey = CACHE_KEY_PREFIX + survey.getId().getValue().toString();
        if (survey.unconfigured())
            //TODO : make TimeUnit DAYS.
            super.writeOnCache(cacheKey, survey, CACHE_TTL_IN_DAYS, TimeUnit.MINUTES);
        else
            super.updateSurveyInDatabase(survey);

    }


}
