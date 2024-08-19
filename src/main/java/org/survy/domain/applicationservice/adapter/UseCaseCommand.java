package org.survy.domain.applicationservice.adapter;

import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.exception.SurveyNotFoundException;
import org.survy.domain.core.valueObject.SurveyId;

import java.util.Optional;
import java.util.UUID;

public abstract class UseCaseCommand {

    protected final SurveyRepository surveyRepository;


    public UseCaseCommand(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    protected Survey fetchSurveyFromDBWithAllAssociations(UUID surveyUUID) {
        Optional<Survey> survey = surveyRepository.findWithQuestionsAndParticipationLogicsById(new SurveyId(surveyUUID));
        if (survey.isEmpty())
            throw new SurveyNotFoundException("No such survey exists: " + surveyUUID);

        return survey.get();
    }
}
