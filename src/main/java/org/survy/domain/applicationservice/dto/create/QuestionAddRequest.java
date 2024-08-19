package org.survy.domain.applicationservice.dto.create;

import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class QuestionAddRequest {
    @NotNull
    private UUID surveyId;
    @NotNull
    private String type;
    @NotNull
    private String questionText;
    @PositiveOrZero
    @Range(min = 0 , max = 10)
    private int page ;
    @PositiveOrZero
    private int order;

    private List<OptionAddRequest> options;





}
