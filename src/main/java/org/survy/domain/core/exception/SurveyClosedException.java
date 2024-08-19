package org.survy.domain.core.exception;

public class SurveyClosedException extends SurveyDomainException {

    public SurveyClosedException(String surveyId) {
        super("survey closed " + surveyId);
    }
}
