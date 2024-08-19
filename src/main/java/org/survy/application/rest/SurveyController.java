package org.survy.application.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.survy.domain.applicationservice.dto.create.*;
import org.survy.domain.applicationservice.ports.input.service.SurveyApplicationService;

@Slf4j
@RestController
@RequestMapping("api/surveys")
public class SurveyController {


    private final SurveyApplicationService surveyApplicationService;

    public SurveyController(SurveyApplicationService surveyApplicationService) {
        this.surveyApplicationService = surveyApplicationService;
    }


    @PostMapping("/create-start")
    public ResponseEntity<SurveyCreationStartResponse> startCreationProcess(@RequestBody SurveyCreationStartRequest request){
        SurveyCreationStartResponse responseBody = surveyApplicationService.startSurveyCreationProcess(request);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/create-add-question")
    public ResponseEntity<QuestionAddResponse> addQuestionToSurvey(@RequestBody QuestionAddRequest request){
        QuestionAddResponse responseBody = surveyApplicationService.addQuestionToSurvey(request);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("create-add-logic")
    public ResponseEntity<LogicAddResponse> addLogicToSurvey(@RequestBody LogicAddRequest request){
        LogicAddResponse responseBody = surveyApplicationService.addParticipationLogicToSurvey(request);
        return ResponseEntity.ok(responseBody);
    }


    @PostMapping("create-complete")
    public ResponseEntity<SurveyCreationCompleteResponse> completeCreationProcess(@RequestBody SurveyCreationCompleteRequest request){
        SurveyCreationCompleteResponse responseBody = surveyApplicationService.completeSurveyCreationProcess(request);
        return ResponseEntity.ok(responseBody);
    }




}
