package org.survy.domain.core.exception;

public class QuestionNotAddedException extends SurveyDomainException{
    public QuestionNotAddedException(String message) {
        super(message);
    }

    public QuestionNotAddedException(String message, Throwable cause) {
        super(message, cause);
    }

}
