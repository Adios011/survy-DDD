package org.survy.domain.applicationservice.dto.participation;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionResponse {
    private String label ;
    private int order;

}
