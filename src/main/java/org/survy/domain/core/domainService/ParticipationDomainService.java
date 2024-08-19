package org.survy.domain.core.domainService;

import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.core.entity.Participation;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.exception.ParticipationDomainException;
import org.survy.domain.core.valueObject.UserId;

public interface ParticipationDomainService {

     Participation startNewParticipationProcess(UserId participantId, Survey survey);

     Participation handleExistingParticipation(Participation participation , UserId participantId , Survey survey) throws ParticipationDomainException;

    Participation answerQuestion(Participation participation , AnswerRequest request);

    void completeParticipation(Participation participation);
}
