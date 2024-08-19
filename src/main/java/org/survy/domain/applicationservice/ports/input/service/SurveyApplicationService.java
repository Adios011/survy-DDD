package org.survy.domain.applicationservice.ports.input.service;

import jakarta.validation.Valid;
import org.survy.domain.applicationservice.dto.create.*;

public interface SurveyApplicationService {

    SurveyCreationStartResponse startSurveyCreationProcess(@Valid SurveyCreationStartRequest request);

    QuestionAddResponse addQuestionToSurvey(@Valid QuestionAddRequest request);

    LogicAddResponse addParticipationLogicToSurvey(@Valid LogicAddRequest request);

    SurveyCreationCompleteResponse completeSurveyCreationProcess(@Valid SurveyCreationCompleteRequest request);
}
