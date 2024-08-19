package org.survy.domain.applicationservice.dto.participation;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationStartResponse {

    private UUID participationId;

    private SurveyResponse survey;
}
