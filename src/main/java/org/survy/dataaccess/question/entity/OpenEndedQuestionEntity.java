package org.survy.dataaccess.question.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.survy.dataaccess.survey.entity.SurveyEntity;
import org.survy.domain.core.valueObject.QuestionTypes;

import java.util.UUID;


@Getter
@Setter
@Entity
@DiscriminatorValue("open-ended")

public class OpenEndedQuestionEntity extends QuestionEntity{



    public OpenEndedQuestionEntity() {

    }

    public OpenEndedQuestionEntity(UUID id, String questionText, int page, int order, SurveyEntity survey) {
        super(id, questionText, page, order, survey , QuestionTypes.OPEN_ENDED.name());
    }

    @Override
    public String getType() {
        return QuestionTypes.OPEN_ENDED.name();
    }

    @Override
    public void update(QuestionEntity newQuestion) {
        if (newQuestion instanceof OpenEndedQuestionEntity openEndedQuestionEntity) {
            super.update(newQuestion);
        }
    }
}
