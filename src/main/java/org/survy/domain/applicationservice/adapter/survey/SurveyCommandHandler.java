package org.survy.domain.applicationservice.adapter.survey;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.exception.SurveyNotFoundException;
import org.survy.domain.core.valueObject.SurveyId;

import java.util.Optional;

@Component
class SurveyCommandHandler {

    private final SurveyRepository surveyRepository;

    public SurveyCommandHandler(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }



}
