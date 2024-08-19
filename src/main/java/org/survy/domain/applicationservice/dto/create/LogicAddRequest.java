package org.survy.domain.applicationservice.dto.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.survy.domain.core.valueObject.QuotaAction;

import java.util.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class LogicAddRequest {

    @NotNull
    private UUID surveyId;

    @NotNull
    @NotEmpty
    private String type;

    @NotNull
    private UUID questionId;

    @NotNull
    private Integer quota ;

    @NotNull
    @NotEmpty
    private List<String> options;

    @NotNull
    private QuotaActionRequest quotaAction;


}
