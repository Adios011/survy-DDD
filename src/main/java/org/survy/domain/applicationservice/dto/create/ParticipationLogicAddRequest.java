package org.survy.domain.applicationservice.dto.create;

import java.util.List;
import java.util.UUID;

public class ParticipationLogicAddRequest {


    private String type ;
    private UUID surveyId;
    private UUID questionId ;
    private int quota;
    private List<String> options;

}
