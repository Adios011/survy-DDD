package org.survy.domain.applicationservice.dto.create;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class SurveyCreationCompleteRequest {

    private UUID surveyId;
}
