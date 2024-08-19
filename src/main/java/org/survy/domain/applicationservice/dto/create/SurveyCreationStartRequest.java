package org.survy.domain.applicationservice.dto.create;





import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SurveyCreationStartRequest {


    @NotNull
    private UUID creatorId;

    @NotNull
    @Length(min = 0 , max = 200)
    private String title;

    @NotNull
    @Length(min = 0 , max = 1000)
    private String description;

    @Future
    @NotNull
    private Date closeDate;



}
