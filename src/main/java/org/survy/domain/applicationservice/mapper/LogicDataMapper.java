package org.survy.domain.applicationservice.mapper;

import org.springframework.stereotype.Component;
import org.survy.domain.applicationservice.dto.create.LogicAddRequest;
import org.survy.domain.applicationservice.dto.create.LogicAddResponse;
import org.survy.domain.core.entity.logic.ParticipationLogic;
import org.survy.domain.core.exception.ParticipationLogicDomainException;
import org.survy.domain.core.factory.logic.SimpleQuotaLogicFactory;
import org.survy.domain.core.valueObject.ParticipationLogicTypes;

@Component
public class LogicDataMapper {

   public  ParticipationLogic logicAddRequestToParticipationLogic(LogicAddRequest request){
        if(request.getType().equalsIgnoreCase(ParticipationLogicTypes.SIMPLE_QUOTA.name()))
            return new SimpleQuotaLogicFactory().create(request);
        else
            throw new ParticipationLogicDomainException("unknown type: " + request.getType());

    }

    public LogicAddResponse participationLogicToLogicAddResponse(ParticipationLogic logicAdded) {
       return new LogicAddResponse(logicAdded.getId());
    }
}
