package org.survy.domain.applicationservice.dto.participation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParticipationStartRequest {

    private UUID userId ;
    private UUID surveyId;


}
