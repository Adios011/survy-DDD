package org.survy.domain.applicationservice.dto.participation;

import java.util.UUID;

public class AnswerResponse {


    private String message;
    private UUID questionId ;
    private UUID participationId;
    private String choiceOptionLabel;
    private String openEndedAnswerText;






    public AnswerResponse(String message , AnswerRequest answerRequest) {
        this.message = message;
        this.questionId = answerRequest.getQuestionId();
        this.participationId = answerRequest.getParticipationId();
        this.choiceOptionLabel = answerRequest.getChoiceOptionLabel();
        this.openEndedAnswerText = answerRequest.getOpenEndedAnswerText();
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

    public UUID getParticipationId() {
        return participationId;
    }

    public void setParticipationId(UUID participationId) {
        this.participationId = participationId;
    }

    public String getChoiceOptionLabel() {
        return choiceOptionLabel;
    }

    public void setChoiceOptionLabel(String choiceOptionLabel) {
        this.choiceOptionLabel = choiceOptionLabel;
    }

    public String getOpenEndedAnswerText() {
        return openEndedAnswerText;
    }

    public void setOpenEndedAnswerText(String openEndedAnswerText) {
        this.openEndedAnswerText = openEndedAnswerText;
    }
}
