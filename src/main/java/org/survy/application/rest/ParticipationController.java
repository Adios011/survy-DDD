package org.survy.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.survy.dataaccess.survey.entity.ParticipationLogicEntity;
import org.survy.dataaccess.survey.entity.SimpleQuotaLogicEntity;
import org.survy.dataaccess.survey.repository.ParticipationLogicJpaRepository;
import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.applicationservice.dto.participation.AnswerResponse;
import org.survy.domain.applicationservice.dto.participation.ParticipationStartRequest;
import org.survy.domain.applicationservice.dto.participation.ParticipationStartResponse;
import org.survy.domain.applicationservice.ports.input.service.ParticipationApplicationService;
import org.survy.domain.core.entity.logic.ParticipationLogic;
import org.survy.domain.core.entity.logic.SimpleQuotaLogic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/participations")
public class ParticipationController {


    @Autowired
    private ParticipationLogicJpaRepository participationLogicJpaRepository;


    private final ParticipationApplicationService participationApplicationService;

    public ParticipationController(ParticipationApplicationService participationApplicationService) {
        this.participationApplicationService = participationApplicationService;
    }

    @PostMapping("start")
    public ResponseEntity<ParticipationStartResponse> startParticipationProcess(@RequestBody ParticipationStartRequest request) {
        ParticipationStartResponse responseBody = participationApplicationService.startParticipationProcess(request);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("answer-question")
    public ResponseEntity<AnswerResponse> answerQuestion(@RequestBody AnswerRequest request) {
        AnswerResponse responseBody = participationApplicationService.answerQuestion(request);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("complete/{participationId}")
    public String completeParticipationProcess(@PathVariable("participationId") UUID participationId) {
        participationApplicationService.completeParticipationProcess(participationId);

        return "Thank you for participating in!";
    }


    @GetMapping("/logic/{id}")
    public String fillQuota(@PathVariable("id") UUID id) {

        ParticipationLogicEntity logic = participationLogicJpaRepository.findById(id).get();

        SimpleQuotaLogicEntity simple = (SimpleQuotaLogicEntity) logic;
        simple.setQuota(0);
        participationLogicJpaRepository.save(simple);

        return "tamamdır.";
    }

}
