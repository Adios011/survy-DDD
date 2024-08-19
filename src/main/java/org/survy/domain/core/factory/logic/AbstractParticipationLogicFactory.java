package org.survy.domain.core.factory.logic;

import org.survy.domain.applicationservice.dto.create.LogicAddRequest;
import org.survy.domain.core.entity.logic.ParticipationLogic;

public  abstract class AbstractParticipationLogicFactory {

    public abstract ParticipationLogic create(LogicAddRequest request);
}
