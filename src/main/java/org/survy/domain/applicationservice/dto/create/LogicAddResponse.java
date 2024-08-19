package org.survy.domain.applicationservice.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class LogicAddResponse {

    private UUID participationLogicId;


}
