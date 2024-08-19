package org.survy.domain.core.entity.question;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.validator.cfg.defs.UUIDDef;
import org.survy.dataaccess.question.entity.OpenEndedQuestionEntity;
import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.core.exception.QuestionDomainException;
import org.survy.domain.core.valueObject.QuestionId;
import org.survy.domain.core.valueObject.QuestionTypes;
import org.survy.domain.core.valueObject.SurveyId;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties("type")
@JsonDeserialize(builder = OpenEndedQuestion.Builder.class)
public class OpenEndedQuestion extends Question {


    private OpenEndedQuestion(Builder builder) {
        super(builder.questionText, builder.page, builder().order);
        setId(builder.id);
        surveyId = builder.surveyId;

    }

    @Override
    public String getType() {
        return QuestionTypes.OPEN_ENDED.name();
    }

    public OpenEndedQuestion(String questionText, int page, int order) {
        super(questionText, page, order);
    }

    public OpenEndedQuestion(QuestionId questionId, String questionText, int page, int order) {
        super(questionText, page, order);
        setId(questionId);
    }

    @Override
    protected void validate(List<String> failureMessages) {

    }


    @Override
    public Answer answer(AnswerRequest answerRequest) {
        QuestionId questionId = new QuestionId(answerRequest.getQuestionId());
        if (!questionId.equals(getId()))
            throw new QuestionDomainException("setId does not match to answer question");

        //TODO : Burada ID'yi oluşturmak ile yanlış yapıyorsun, onu Answer'ın içine bırakmak lazım.
        return Answer.builder()
                .id(UUID.randomUUID())
                .answerText(answerRequest.getOpenEndedAnswerText())
                .questionId(this.getId())
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private QuestionId id;
        private String questionText;
        private int page;
        private int order;
        private SurveyId surveyId;
        private String type;

        private Builder() {
        }

        @JsonProperty("surveyId")
        public Builder surveyId(SurveyId val){
            surveyId = val;
            return this;
        }

        @JsonProperty("type")
        public Builder type(String val){
            this.type = val;
            return this;
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

        public OpenEndedQuestion build() {
            return new OpenEndedQuestion(this);
        }
    }
}
