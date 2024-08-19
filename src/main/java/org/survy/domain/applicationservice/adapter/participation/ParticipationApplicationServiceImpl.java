package org.survy.domain.applicationservice.adapter.participation;


import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.applicationservice.dto.participation.AnswerResponse;
import org.survy.domain.applicationservice.dto.participation.ParticipationStartRequest;
import org.survy.domain.applicationservice.dto.participation.ParticipationStartResponse;
import org.survy.domain.applicationservice.ports.input.service.ParticipationApplicationService;

import java.util.UUID;

@Service("participationApplicationService")
public class ParticipationApplicationServiceImpl implements ParticipationApplicationService {

    private final StartParticipationCommand startParticipationCommand;
    private final AnswerQuestionCommand answerQuestionCommand;
    private final CompleteParticipationCommand completeParticipationCommand;

    public ParticipationApplicationServiceImpl(StartParticipationCommand startParticipationCommand,
                                               AnswerQuestionCommand answerQuestionCommand,
                                               CompleteParticipationCommand completeParticipationCommand) {
        this.startParticipationCommand = startParticipationCommand;
        this.answerQuestionCommand = answerQuestionCommand;
        this.completeParticipationCommand = completeParticipationCommand;
    }

    @Override
    public ParticipationStartResponse startParticipationProcess(ParticipationStartRequest request) {
        return startParticipationCommand.startParticipationProcess(request);
    }

    @Override
    public AnswerResponse answerQuestion(AnswerRequest request) {
        return answerQuestionCommand.answerQuestion(request);
    }

    @Override
    public void completeParticipationProcess(@NotNull UUID participationId) {
        completeParticipationCommand.completeParticipation(participationId);
    }
}
