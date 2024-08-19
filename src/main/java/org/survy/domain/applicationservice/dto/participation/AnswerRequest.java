package org.survy.domain.applicationservice.dto.participation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AnswerRequest {

    private UUID surveyId;
    private UUID questionId ;
    private UUID participationId ;

    private String choiceOptionLabel;
    private String openEndedAnswerText;


    public UUID getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(UUID surveyId) {
        this.surveyId = surveyId;
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
