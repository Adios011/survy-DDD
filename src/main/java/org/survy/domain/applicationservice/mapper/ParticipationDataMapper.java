package org.survy.domain.applicationservice.mapper;

import org.springframework.stereotype.Component;
import org.survy.domain.applicationservice.dto.participation.ParticipationStartResponse;
import org.survy.domain.core.entity.Participation;

@Component
public class ParticipationDataMapper {

    private final SurveyDataMapper surveyDataMapper;

    public ParticipationDataMapper(SurveyDataMapper surveyDataMapper) {
        this.surveyDataMapper = surveyDataMapper;
    }

    public ParticipationStartResponse toParticipationStartResponse(Participation participation) {

        return ParticipationStartResponse.builder()
                .participationId(participation.getId().getValue())
                .survey(surveyDataMapper.toSurveyResponse(participation.getSurvey()))
                .build();
    }
}
