package org.survy.domain.core.exception;

public class SurveyDomainException extends DomainException{

    public SurveyDomainException(String message) {
        super(message);
    }

    public SurveyDomainException(String message, Throwable cause) {
        super(message, cause);
    }

}
