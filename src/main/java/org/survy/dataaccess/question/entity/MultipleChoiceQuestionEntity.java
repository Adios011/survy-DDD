package org.survy.dataaccess.question.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.survy.dataaccess.converter.JsonAttributeConverter;
import org.survy.dataaccess.survey.entity.SurveyEntity;
import org.survy.domain.core.valueObject.Option;
import org.survy.domain.core.valueObject.QuestionTypes;

import java.util.List;
import java.util.UUID;


@Entity
@DiscriminatorValue("multiple-choice")

public class MultipleChoiceQuestionEntity extends QuestionEntity {


    @Column(length = 2000)
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Option> options;


    public MultipleChoiceQuestionEntity() {

    }

    @Override
    public String getType() {
        return QuestionTypes.MULTIPLE_CHOICE.name();
    }

    @Override
    public void update(QuestionEntity newQuestion) {
        if (newQuestion instanceof MultipleChoiceQuestionEntity newMultipleChoice) {
            super.update(newQuestion);
            this.options = newMultipleChoice.options;
        }

    }

    public MultipleChoiceQuestionEntity(UUID id, String questionText, int page, int order, SurveyEntity survey, List<Option> options) {
        super(id, questionText, page, order, survey, QuestionTypes.MULTIPLE_CHOICE.name());
        this.options = options;
    }


    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }


}
