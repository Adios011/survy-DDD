package org.survy.dataaccess.question.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.survy.dataaccess.survey.entity.SurveyEntity;
import org.survy.domain.core.valueObject.QuestionTypes;
import org.survy.domain.core.valueObject.WeightedOption;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Entity
@DiscriminatorValue("rating-scale")

public class RatingScaleQuestionEntity extends QuestionEntity {


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "options")
    private List<WeightedOption> weightedOptions;

    public RatingScaleQuestionEntity() {

    }

    @Override
    public String getType() {
        return QuestionTypes.RATING_SCALE.name();
    }

    @Override
    public void update(QuestionEntity newQuestion) {
        if (newQuestion instanceof RatingScaleQuestionEntity ratingScaleQuestionEntity) {
            super.update(newQuestion);
            this.weightedOptions = ratingScaleQuestionEntity.weightedOptions;
        }
    }

    public RatingScaleQuestionEntity(UUID id, String questionText, int page, int order, SurveyEntity survey, List<WeightedOption> weightedOptions) {
        super(id, questionText, page, order, survey, QuestionTypes.RATING_SCALE.name());
        this.weightedOptions = weightedOptions;
    }


    public List<WeightedOption> getWeightedOptions() {
        return weightedOptions;
    }

    public void setWeightedOptions(List<WeightedOption> weightedOptions) {
        this.weightedOptions = weightedOptions;
    }
}
