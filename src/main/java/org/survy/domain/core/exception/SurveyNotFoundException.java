package org.survy.domain.core.exception;



public class SurveyNotFoundException extends NotFoundException {

    public SurveyNotFoundException(String surveyId) {
        super("No such survey found: " + surveyId);
    }
}
