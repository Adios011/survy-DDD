package org.survy.domain.core.entity.question;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.core.exception.QuestionDomainException;
import org.survy.domain.core.exception.messages.InputRules;
import org.survy.domain.core.valueObject.Option;
import org.survy.domain.core.valueObject.QuestionId;
import org.survy.domain.core.valueObject.QuestionTypes;
import org.survy.domain.core.valueObject.SurveyId;


import java.util.List;
import java.util.UUID;

@JsonDeserialize(builder = MultipleChoiceQuestion.Builder.class)
public class MultipleChoiceQuestion extends Question implements QuotaApplicableQuestion {

    private List<Option> options;




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


    public MultipleChoiceQuestion(String questionText, int page, int order, List<Option> options) {
        super(questionText, page, order);
        this.options = options;
    }

    public MultipleChoiceQuestion(QuestionId questionId, String questionText, int page, int order, List<Option> options) {
        super(questionText, page, order);
        setId(questionId);
        this.options = options;
    }

    private MultipleChoiceQuestion(Builder builder) {
        super(builder.questionText, builder.page, builder.order);
        setId(builder.id);
        surveyId = builder.surveyId;
        options = builder.options;
    }


    public List<Option> getOptions() {
        return options;
    }

    @Override
    public String getType() {
        return QuestionTypes.MULTIPLE_CHOICE.name();
    }

    @Override
    protected void validate(List<String> failureMessages) {
        if (options == null || options.size() < 2)
            failureMessages.add(InputRules.MULTIPLE_CHOICE_OPTION_NUMBER);

        for (Option option : options) {
            if (!option.valid())
                failureMessages.add(InputRules.INVALID_OPTION);
        }
    }

    @Override
    public boolean containsOptionByLabel(String optionLabel) {
        return options.stream().anyMatch(option -> option.getLabel().equals(optionLabel));
    }


    public static Builder builder() {
        return new Builder();
    }


    @JsonIgnoreProperties("type")
    public static final class Builder {
        private QuestionId id;
        private String questionText;
        private int order;
        private int page;
        private List<Option> options;
        private SurveyId surveyId;

        private Builder() {
        }

        @JsonProperty("id")
        public Builder id(QuestionId val) {
            id = val;
            return this;
        }

        @JsonProperty("options")
        public Builder options(List<Option> val) {
            options = val;
            return this;
        }

        @JsonProperty("surveyId")
        public Builder surveyId(SurveyId val) {
            surveyId = val;
            return this;
        }

        @JsonProperty("questionText")
        public Builder questionText(String val) {
            questionText = val;
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


        public MultipleChoiceQuestion build() {
            return new MultipleChoiceQuestion(this);
        }
    }
}
