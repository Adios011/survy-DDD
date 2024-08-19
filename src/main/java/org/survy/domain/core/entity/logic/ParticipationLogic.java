package org.survy.domain.core.entity.logic;

import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.core.entity.BaseEntity;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.entity.question.Answer;
import org.survy.domain.core.exception.ParticipationLogicInitializationException;
import org.survy.domain.core.exception.messages.FailureMessages;
import org.survy.domain.core.valueObject.QuestionId;
import org.survy.domain.core.valueObject.SurveyId;

import java.util.*;

public abstract class ParticipationLogic extends BaseEntity<UUID> {

    protected SurveyId surveyId;

    public ParticipationLogic() {

    }

    public ParticipationLogic(SurveyId surveyId) {
        this.surveyId = surveyId;
    }

    public abstract void validateWithSurvey(Survey survey);

    public void initialize(Survey survey) {
        validateWithSurvey(survey);
        this.surveyId = survey.getId();
        setId(UUID.randomUUID());

    }


    public SurveyId getSurveyId() {
        return surveyId;
    }

    public abstract boolean canBeParticipatedInSurvey(Set<String> failureMessages);

    public abstract boolean canQuestionBeAnswered(QuestionId questionId, AnswerRequest answer);

    public abstract boolean canParticipationBeCompleted(Map<QuestionId, Answer> questionIdAnswerMap);

    public abstract void updateLogicData(Map<QuestionId, Answer> questionIdAnswerMap);
}
