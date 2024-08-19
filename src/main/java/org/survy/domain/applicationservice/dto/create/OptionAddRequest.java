package org.survy.domain.applicationservice.dto.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class OptionAddRequest {

    @NotNull
    @NotEmpty
    private String label;
    @PositiveOrZero
    private int order;
    @Positive
    private int weight ;



}
