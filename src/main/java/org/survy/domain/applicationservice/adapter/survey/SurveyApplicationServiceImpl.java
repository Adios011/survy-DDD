package org.survy.domain.applicationservice.adapter.survey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.survy.domain.applicationservice.dto.create.*;
import org.survy.domain.applicationservice.ports.input.service.SurveyApplicationService;

@Service("surveyApplicationService")
@Slf4j
@Validated
class SurveyApplicationServiceImpl implements SurveyApplicationService {

    private final StartSurveyCreationCommand startSurveyCreationCommand;
    private final AddQuestionToSurveyCommand addQuestionToSurveyCommand;
    private final AddParticipationLogicToSurveyCommand addParticipationLogicToSurveyCommand;
    private final CompleteSurveyCreationCommand completeSurveyCreationCommand;

    public SurveyApplicationServiceImpl(StartSurveyCreationCommand startSurveyCreationCommand,
                                        AddQuestionToSurveyCommand addQuestionToSurveyCommand,
                                        AddParticipationLogicToSurveyCommand addParticipationLogicToSurveyCommand,
                                        CompleteSurveyCreationCommand completeSurveyCreationCommand) {
        this.startSurveyCreationCommand = startSurveyCreationCommand;
        this.addQuestionToSurveyCommand = addQuestionToSurveyCommand;
        this.addParticipationLogicToSurveyCommand = addParticipationLogicToSurveyCommand;
        this.completeSurveyCreationCommand = completeSurveyCreationCommand;
    }

    @Override
    public SurveyCreationStartResponse startSurveyCreationProcess(SurveyCreationStartRequest request) {
        return startSurveyCreationCommand.startSurveyCreationProcess(request);
    }

    @Override
    public QuestionAddResponse addQuestionToSurvey(QuestionAddRequest request) {
        return addQuestionToSurveyCommand.addQuestionToSurvey(request);
    }

    @Override
    public LogicAddResponse addParticipationLogicToSurvey(LogicAddRequest request) {
        return addParticipationLogicToSurveyCommand.addParticipationLogicToSurvey(request);
    }

    @Override
    public SurveyCreationCompleteResponse completeSurveyCreationProcess(SurveyCreationCompleteRequest request) {
        return completeSurveyCreationCommand.completeSurveyCreationProcess(request);
    }
}
