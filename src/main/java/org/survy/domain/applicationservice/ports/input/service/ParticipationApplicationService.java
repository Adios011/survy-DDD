package org.survy.domain.applicationservice.ports.input.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.applicationservice.dto.participation.AnswerResponse;
import org.survy.domain.applicationservice.dto.participation.ParticipationStartRequest;
import org.survy.domain.applicationservice.dto.participation.ParticipationStartResponse;

import java.util.UUID;

public interface ParticipationApplicationService {

    ParticipationStartResponse startParticipationProcess(@Valid ParticipationStartRequest request);

    AnswerResponse answerQuestion(@Valid AnswerRequest request);

    void completeParticipationProcess(@NotNull UUID participationId);
}
