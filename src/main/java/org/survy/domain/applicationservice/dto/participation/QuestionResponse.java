package org.survy.domain.applicationservice.dto.participation;

import lombok.*;
import org.survy.domain.core.valueObject.Option;
import org.survy.domain.core.valueObject.WeightedOption;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {

    private UUID questionId ;
    private String type;
    private String questionText;
    private int page;
    private int order;
    private List<OptionResponse> options;

}
