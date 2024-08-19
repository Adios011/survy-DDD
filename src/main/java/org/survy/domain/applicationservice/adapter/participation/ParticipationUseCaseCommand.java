package org.survy.domain.applicationservice.adapter.participation;

import org.survy.domain.applicationservice.adapter.UseCaseCommand;
import org.survy.domain.applicationservice.ports.output.repository.ParticipationRepository;
import org.survy.domain.applicationservice.ports.output.repository.SurveyRepository;
import org.survy.domain.core.entity.Participation;
import org.survy.domain.core.exception.ParticipationDomainException;

public abstract class ParticipationUseCaseCommand extends UseCaseCommand {

    protected final ParticipationRepository participationRepository;


    public ParticipationUseCaseCommand(ParticipationRepository participationRepository , SurveyRepository surveyRepository) {
        super(surveyRepository);
        this.participationRepository = participationRepository;
    }



}
