package org.survy.dataaccess.survey.mapper;

import org.springframework.stereotype.Component;
import org.survy.dataaccess.survey.entity.ParticipationLogicEntity;
import org.survy.dataaccess.survey.entity.SimpleQuotaLogicEntity;
import org.survy.domain.core.entity.logic.ParticipationLogic;
import org.survy.domain.core.entity.logic.SimpleQuotaLogic;
import org.survy.domain.core.valueObject.QuestionId;
import org.survy.domain.core.valueObject.SurveyId;

import java.util.*;

@Component
public class ParticipationLogicDataAccessMapper {


    public List<ParticipationLogicEntity> toEntities(List<ParticipationLogic> fromList) {
        List<ParticipationLogicEntity> toList = new ArrayList<>();

        for (ParticipationLogic participationLogic : fromList) {
            if(participationLogic == null)
                continue;

            if (participationLogic instanceof SimpleQuotaLogic simpleQuotaLogic)
                toList.add(toSimpleQuotaLogicEntity(simpleQuotaLogic));
            else
                throw new RuntimeException("Unknown subtype " + participationLogic.getClass().getName());
        }

        return toList;
    }

    private SimpleQuotaLogicEntity toSimpleQuotaLogicEntity(SimpleQuotaLogic from) {
        SimpleQuotaLogicEntity to = new SimpleQuotaLogicEntity();
        to.setId(from.getId());
        to.setQuota(from.getQuota());
        to.setQuestionId(from.getQuestionId().getValue());
        to.setOptionCurrentMap(from.getOptionCurrentMap());
        to.setQuotaAction(from.getQuotaAction());
        return to;
    }







    public List<ParticipationLogic> toDomainList(Set<ParticipationLogicEntity> jpaList) {
        if (jpaList == null)
            return null;

        List<ParticipationLogic> domainList = new ArrayList<>() ;

        for (ParticipationLogicEntity from : jpaList) {
            if (from == null)
                continue;


            if (from instanceof SimpleQuotaLogicEntity simpleQuotaLogicEntity)
                domainList.add(toSimpleQuotaLogic(simpleQuotaLogicEntity));
            else
                throw new RuntimeException("unknown subtype " + from.getClass().getName());
        }

        return domainList;
    }

    public SimpleQuotaLogic toSimpleQuotaLogic(SimpleQuotaLogicEntity from) {
        return SimpleQuotaLogic.builder()
                .id(from.getId())
                .quota(from.getQuota())
                .optionCurrentMap(new HashMap<>(from.getOptionCurrentMap()))
                .questionId(new QuestionId(from.getQuestionId()))
                .surveyId(new SurveyId(from.getSurvey().getId()))
                .quotaAction(from.getQuotaAction())
                .build();
    }


}
