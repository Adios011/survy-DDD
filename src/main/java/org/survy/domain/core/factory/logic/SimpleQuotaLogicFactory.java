package org.survy.domain.core.factory.logic;

import org.survy.domain.applicationservice.dto.create.LogicAddRequest;
import org.survy.domain.applicationservice.dto.create.QuotaActionRequest;
import org.survy.domain.core.entity.logic.SimpleQuotaLogic;
import org.survy.domain.core.valueObject.QuestionId;
import org.survy.domain.core.valueObject.QuotaAction;
import org.survy.domain.core.valueObject.QuotaActionTypes;

import java.util.Collections;
import java.util.HashMap;

public class SimpleQuotaLogicFactory extends AbstractParticipationLogicFactory {


    @Override
    public SimpleQuotaLogic create(LogicAddRequest request) {
        System.out.println(request.getQuotaAction());

        HashMap<String, Integer> map = new HashMap<>();
        request.getOptions().forEach(optionLabel -> map.put(optionLabel, SimpleQuotaLogic.CURRENT_INITIAL_VALUE));


        return SimpleQuotaLogic.builder()
                .quota(request.getQuota())
                .questionId(new QuestionId(request.getQuestionId()))
                .optionCurrentMap(map)
                .quotaAction(toQuotaAction(request.getQuotaAction()))
                .build();



    }

    private QuotaAction toQuotaAction(QuotaActionRequest request) {
        return new QuotaAction(QuotaActionTypes.from(request.getActionType()),
                request.getUrl(),
                request.getCustomMessage()
        );
    }
}
