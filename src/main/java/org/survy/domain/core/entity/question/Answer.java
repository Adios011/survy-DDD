package org.survy.domain.core.entity.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.survy.domain.core.valueObject.QuestionId;

import java.util.UUID;

@JsonDeserialize(builder = Answer.Builder.class)
public class Answer {

    private UUID id ;

    private String answerText ;

    private QuestionId questionId;


    public Answer(UUID id) {
        this.id = id;
    }



    private Answer(Builder builder) {
        id = builder.id;
        answerText = builder.answerText;
        questionId = builder.questionId;
    }


    public void update(Answer answer){
        this.id = answer.getId();
        this.answerText = answer.getAnswerText();
        this.questionId = answer.getQuestionId();
    }

    public static Builder builder() {
        return new Builder();
    }



    public UUID getId() {
        return id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public QuestionId getQuestionId() {
        return questionId;
    }


    @Override
    public String toString() {
        return "Answer{" +
                "setId=" + id +
                ", answerText='" + answerText + '\'' +
                ", questionId=" + questionId +
                '}';
    }


    public static final class Builder {
        private UUID id;
        private String answerText;
        private QuestionId questionId;

        private Builder() {
        }



        @JsonProperty("id")
        public Builder id(UUID val) {
            id = val;
            return this;
        }
        @JsonProperty("answerText")
        public Builder answerText(String val) {
            answerText = val;
            return this;
        }
        @JsonProperty("questionId")
        public Builder questionId(QuestionId val) {
            questionId = val;
            return this;
        }

        public Answer build() {
            return new Answer(this);
        }
    }
}
