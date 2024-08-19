package org.survy.domain.applicationservice.ports.output.repository;

import org.survy.domain.core.entity.Participation;
import org.survy.domain.core.valueObject.ParticipationId;
import org.survy.domain.core.valueObject.ParticipationStatus;

import java.util.Optional;
import java.util.UUID;

public interface ParticipationRepository {


    boolean save(Participation participation);

    boolean update(Participation participation);

    Optional<Participation> findByUserIdAndSurveyId(UUID userId, UUID surveyId);


    boolean updateStatus(ParticipationId participationId, ParticipationStatus participationStatus);


    Optional<Participation> findToAnswerQuestion(UUID participationId);
}
