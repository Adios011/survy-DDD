package org.survy.domain.applicationservice.dto.participation;
import lombok.*;
import org.survy.domain.core.valueObject.SurveyStatus;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyResponse {


    private List<QuestionResponse> questions;



}
