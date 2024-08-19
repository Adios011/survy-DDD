package org.survy.domain.applicationservice.dto.create;

import java.util.UUID;

public class SurveyCreationStartResponse {

    private UUID surveyId;

    public SurveyCreationStartResponse(UUID surveyId) {
        this.surveyId = surveyId;
    }

    public UUID getSurveyId() {
        return surveyId;
    }
}
