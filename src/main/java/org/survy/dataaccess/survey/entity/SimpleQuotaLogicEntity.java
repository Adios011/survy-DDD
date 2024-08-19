package org.survy.dataaccess.survey.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.survy.domain.core.entity.logic.ParticipationLogic;
import org.survy.domain.core.entity.logic.SimpleQuotaLogic;
import org.survy.domain.core.valueObject.ParticipationLogicTypes;
import org.survy.domain.core.valueObject.QuotaAction;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor

@Entity
@DiscriminatorValue("simple-quota")
public class SimpleQuotaLogicEntity extends ParticipationLogicEntity {

    @Column(nullable = false, columnDefinition = "int default 0")
    private int quota;

    @Column(nullable = false)
    private UUID questionId;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Integer> optionCurrentMap;

    @JdbcTypeCode(SqlTypes.JSON)
    private QuotaAction quotaAction;




    public void setQuota(int quota) {
        this.quota = quota;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public void setOptionCurrentMap(Map<String, Integer> optionCurrentMap) {
        this.optionCurrentMap = optionCurrentMap;
    }

    @Override
    public String toString() {
        return "SimpleQuotaLogicEntity{" +
                "quota=" + quota +
                ", questionId=" + questionId +
                ", optionCurrentMap=" + optionCurrentMap +
                '}';
    }

    public int getQuota() {
        return quota;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public Map<String, Integer> getOptionCurrentMap() {
        return optionCurrentMap;
    }

    public QuotaAction getQuotaAction() {
        return quotaAction;
    }

    public void setQuotaAction(QuotaAction quotaAction) {
        this.quotaAction = quotaAction;
    }

    @Override
    public String getType() {
        return ParticipationLogicTypes.SIMPLE_QUOTA.name();
    }

    @Override
    public void update(ParticipationLogicEntity newLogic) {
        if(newLogic instanceof SimpleQuotaLogicEntity newSimpleQuota){
            super.update(newLogic);
            this.quota = newSimpleQuota.quota;
            this.optionCurrentMap = newSimpleQuota.optionCurrentMap;
            this.questionId = newSimpleQuota.questionId;
            this.quotaAction = newSimpleQuota.quotaAction;
        }
    }
}
