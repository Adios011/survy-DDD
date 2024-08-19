package org.survy.domain.applicationservice.dto.cache;

import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.entity.question.Answer;
import org.survy.domain.core.valueObject.*;

import java.util.*;
public class ParticipationCacheModel {

    private ParticipationId participationId;
    private UserId userId;
    private SurveyId  surveyId;
    private ParticipationStatus participationStatus;
    private Set<QuestionId> answeredQuestionIds;
    private List<Answer> answers;


    public ParticipationCacheModel() {
    }

    public ParticipationCacheModel(ParticipationStatus participationStatus, List<Answer> answers) {
        this.participationStatus = participationStatus;
        this.answers = answers;
    }

    public ParticipationStatus getParticipationStatus() {
        return participationStatus;
    }

    public void setParticipationStatus(ParticipationStatus participationStatus) {
        this.participationStatus = participationStatus;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public ParticipationId getParticipationId() {
        return participationId;
    }

    public void setParticipationId(ParticipationId participationId) {
        this.participationId = participationId;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public SurveyId getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(SurveyId surveyId) {
        this.surveyId = surveyId;
    }

    public Set<QuestionId> getAnsweredQuestionIds() {
        return answeredQuestionIds;
    }

    public void setAnsweredQuestionIds(Set<QuestionId> answeredQuestionIds) {
        this.answeredQuestionIds = answeredQuestionIds;
    }

    @Override
    public String toString() {
        return "ParticipationCacheModel{" +
                "participationId=" + participationId +
                ", userId=" + userId +
                ", surveyId=" + surveyId +
                ", participationStatus=" + participationStatus +
                ", answeredQuestionIds=" + answeredQuestionIds +
                ", answers=" + answers +
                '}';
    }
}
