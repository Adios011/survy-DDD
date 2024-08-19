package org.survy.domain.core.event;

import org.survy.domain.core.valueObject.SurveyId;

public class SurveyClosedEvent extends DomainEvent{

    private final SurveyId surveyId;
    private final String reason;

    public SurveyClosedEvent(SurveyId surveyId, String reason) {
        this.surveyId = surveyId;
        this.reason = reason;
    }

    public SurveyId getSurveyId() {
        return surveyId;
    }

    public String getReason() {
        return reason;
    }
}
