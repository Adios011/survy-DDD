package org.survy.domain.applicationservice.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Data
public class QuestionAddResponse {

    private UUID questionId ;
    private UUID surveyId ;
    private String questionText ;

}
