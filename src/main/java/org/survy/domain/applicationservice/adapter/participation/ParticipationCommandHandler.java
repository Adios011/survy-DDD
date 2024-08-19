package org.survy.domain.applicationservice.adapter.participation;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.survy.domain.applicationservice.ports.output.repository.ParticipationRepository;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.exception.ParticipationDomainException;
import org.survy.domain.core.exception.SurveyDomainException;
import org.survy.domain.core.valueObject.ParticipationId;
import org.survy.domain.core.valueObject.ParticipationStatus;
import org.survy.domain.core.valueObject.SurveyId;
import org.survy.domain.core.valueObject.SurveyStatus;

@Component
 class ParticipationCommandHandler {

     private final ParticipationRepository participationRepository;
     private final SurveyRepository surveyRepository;

    public ParticipationCommandHandler(ParticipationRepository participationRepository, SurveyRepository surveyRepository) {
        this.participationRepository = participationRepository;
        this.surveyRepository = surveyRepository;
    }

    @Async
    void updateParticipationStatus(ParticipationId participationId, ParticipationStatus newStatus) {
        boolean isUpdated = participationRepository.updateStatus(participationId,newStatus);
        if(!isUpdated)
            throw new ParticipationDomainException("status could not updated!");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateSurveyStatus(SurveyId surveyId , SurveyStatus newStatus){
        boolean isUpdated = surveyRepository.updateStatus(surveyId , newStatus , "") ;
        if(!isUpdated)
            throw new SurveyDomainException("status could not updated!");
    }

}
