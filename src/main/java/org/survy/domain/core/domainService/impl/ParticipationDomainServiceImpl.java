package org.survy.domain.core.domainService.impl;

import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.core.domainService.ParticipationDomainService;
import org.survy.domain.core.entity.Participation;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.exception.ParticipationDomainException;
import org.survy.domain.core.exception.messages.FailureMessages;
import org.survy.domain.core.valueObject.UserId;

import java.util.*;


public class ParticipationDomainServiceImpl implements ParticipationDomainService {


    @Override
    public Participation startNewParticipationProcess(UserId participantId, Survey survey) {
        return Participation.startParticipationProcess(participantId, survey);
    }


    @Override
    public Participation handleExistingParticipation(Participation participation, UserId participantId, Survey survey) throws ParticipationDomainException {
        return participation.handleInProgressParticipation(participantId, survey);
    }


    @Override
    public Participation answerQuestion(Participation participation, AnswerRequest request) {
        return participation.answerQuestion(request);
    }

    @Override
    public void completeParticipation(Participation participation) {
        participation.complete();
    }
}
