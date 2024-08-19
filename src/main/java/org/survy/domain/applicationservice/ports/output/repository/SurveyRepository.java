package org.survy.domain.applicationservice.ports.output.repository;

import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.valueObject.SurveyId;
import org.survy.domain.core.valueObject.SurveyStatus;

import java.util.Optional;

public interface SurveyRepository {

    boolean save(Survey survey);

    boolean update(Survey survey);

    boolean updateWithAllAssociations(Survey survey);

    boolean updateStatus(SurveyId surveyId , SurveyStatus newStatus, String reason);

    Optional<Survey> findById(SurveyId surveyId);

    Optional<Survey> findWithQuestionsById(SurveyId surveyId);

    Optional<Survey> findWithParticipationLogicsById(SurveyId surveyId);

    Optional<Survey> findWithQuestionsAndParticipationLogicsById(SurveyId surveyId);
}
