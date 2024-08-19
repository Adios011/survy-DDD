package org.survy.domain.core.entity.question;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.core.exception.QuestionDomainException;
import org.survy.domain.core.exception.messages.InputRules;
import org.survy.domain.core.valueObject.*;

import java.util.List;
import java.util.UUID;

@JsonDeserialize(builder = RatingScaleQuestion.Builder.class)
public class RatingScaleQuestion extends Question implements QuotaApplicableQuestion {

    private List<WeightedOption> weightedOptions;

    public RatingScaleQuestion(QuestionId questionId, String questionText, int page, int order, List<WeightedOption> options) {
        super(questionText, page, order);
        setId(questionId);
        this.weightedOptions = options;
    }

    public RatingScaleQuestion(String questionText, int page, int order, List<WeightedOption> options) {
        super(questionText, page, order);
        this.weightedOptions = options;
    }

    private RatingScaleQuestion(Builder builder) {
        super(builder.questionText , builder.page , builder.order);
        setId(builder.id);
        weightedOptions = builder.weightedOptions;
        surveyId = builder.surveyId;
    }


    @Override
    public Answer answer(AnswerRequest answerRequest) {
        QuestionId questionId = new QuestionId(answerRequest.getQuestionId());
        if (!questionId.equals(getId()))
            throw new QuestionDomainException("setId does not match to answer question");

        String choiceOptionLabel = answerRequest.getChoiceOptionLabel();
        if (!containsOptionByLabel(choiceOptionLabel))
            throw new QuestionDomainException("question does NOT have such option  " + choiceOptionLabel);

        return Answer.builder()
                .id(UUID.randomUUID())
                .answerText(choiceOptionLabel)
                .questionId(this.getId())
                .build();

    }

    @Override
    public String getType() {
        return QuestionTypes.RATING_SCALE.name();
    }

    public RatingScaleQuestion(String questionText, int page, int order) {
        super(questionText, page, order);
    }






    @Override
    protected void validate(List<String> failureMessages) {
        if (weightedOptions == null || weightedOptions.isEmpty() || weightedOptions.size() < 2)
            throw new QuestionDomainException(InputRules.MULTIPLE_CHOICE_OPTION_NUMBER);

        for (Option option : weightedOptions) {
            if (!option.valid())
                throw new QuestionDomainException(InputRules.INVALID_OPTION);
        }
    }

    public List<WeightedOption> getWeightedOptions() {
        return weightedOptions;
    }

    @Override
    public boolean containsOptionByLabel(String optionLabel) {
        return weightedOptions.stream().anyMatch(option -> option.getLabel().equals(optionLabel));
    }



    public static Builder builder() {
        return new Builder();
    }


    @JsonIgnoreProperties("type")
    public static final class Builder {
        private QuestionId id;
        private String questionText;
        private int page;
        private int order;
        private List<WeightedOption> weightedOptions;
        private SurveyId surveyId;

        private Builder() {
        }

        @JsonProperty("id")
        public Builder id(QuestionId val) {
            id = val;
            return this;
        }
        @JsonProperty("questionText")
        public Builder questionText(String val) {
            questionText = val;
            return this;
        }
        @JsonProperty("surveyId")
        public Builder surveyId(SurveyId val){
            surveyId = val;
            return this;
        }
        @JsonProperty("page")
        public Builder page(int val) {
            page = val;
            return this;
        }
        @JsonProperty("order")
        public Builder order(int val) {
            order = val;
            return this;
        }
        @JsonProperty("weightedOptions")
        public Builder weightedOptions(List<WeightedOption> val) {
            weightedOptions = val;
            return this;
        }

        public RatingScaleQuestion build() {
            return new RatingScaleQuestion(this);
        }
    }
}
