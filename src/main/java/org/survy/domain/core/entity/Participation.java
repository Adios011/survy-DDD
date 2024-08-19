package org.survy.domain.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.core.entity.question.Answer;
import org.survy.domain.core.exception.*;
import org.survy.domain.core.exception.messages.FailureMessages;
import org.survy.domain.core.valueObject.*;

import java.util.*;

@JsonDeserialize(builder = Participation.Builder.class)
public class Participation extends AggregateRoot<ParticipationId> {

    private UserId userId;
    private Survey survey;
    private ParticipationStatus participationStatus;

    private List<Answer> answers;
    private Set<QuestionId> answeredQuestionIds;

    private Participation(Builder builder) {
        setId(builder.id);
        userId = builder.userId;
        survey = builder.survey;
        participationStatus = builder.participationStatus;
        answers = builder.answers;
        setAnsweredQuestionIds(builder.answeredQuestionIds);
    }


    public Participation answerQuestion(AnswerRequest request) {
        if (participationStatus == null || !inProgress())
            throw new ParticipationDomainException("participation is not in correct status to answer question");


        QuestionId questionId = new QuestionId(request.getQuestionId());

        try {
            Answer answer = survey.answerQuestion(questionId, request);
            if (answeredQuestionIds.contains(questionId)) {
                replaceAnswer(questionId, answer);
            } else {
                addQuestionIdToAnsweredQuestions(questionId);
                addAnswer(answer);
            }
            return this;
        } catch (ParticipationDomainException participationDomainException) {
            cancelParticipation();
            throw participationDomainException;
        }


    }

    private void replaceAnswer(QuestionId questionId, Answer newAnswer) {
        Answer oldAnswer = answers.stream().filter(answer -> answer.getQuestionId().equals(questionId)).findFirst().orElse(null);
        if (oldAnswer == null)
            return;

        oldAnswer.update(newAnswer);
    }

    private void removeOldAnswer(QuestionId questionId) {
        answers.removeIf(answer -> answer.getQuestionId().equals(questionId));
    }


    private void addQuestionIdToAnsweredQuestions(QuestionId questionId) {
        if (answeredQuestionIds == null) answeredQuestionIds = new HashSet<>();

        answeredQuestionIds.add(questionId);
    }

    private void addAnswer(Answer answer) {
        if (answers == null) answers = new ArrayList<>();

        answers.add(answer);
    }

    public boolean inProgress() {
        return participationStatus.equals(ParticipationStatus.IN_PROGRESS);
    }


    public void setAnsweredQuestionIds(Set<QuestionId> questionIds) {
        this.answeredQuestionIds = questionIds;
    }

    public boolean completed() {
        return (participationStatus != null) && (participationStatus == ParticipationStatus.COMPLETED);
    }


    private void checkInProgressParticipation(UserId participantId) {
        if (!participantId.equals(userId))
            throw new ParticipationDomainException("this participation does not belong to this user " + userId.getValue());

        if (completed()) throw new ParticipationDomainException("has already been participated in!");
    }


    private void cancelParticipation() {
        this.participationStatus = ParticipationStatus.CANCELED;
    }

    public Participation handleInProgressParticipation(UserId participantId, Survey survey) throws SurveyDomainException {
        checkInProgressParticipation(participantId);
        this.survey = survey;
        checkSurveyCanBeParticipatedIn(survey);
        survey.filterQuestions(answeredQuestionIds);
        return this;
    }

    public static Participation startParticipationProcess(UserId participantId, Survey survey) throws ParticipationDomainException {
        checkSurveyCanBeParticipatedIn(survey);

        return Participation.builder()
                .survey(survey)
                .participationId(new ParticipationId(UUID.randomUUID()))
                .userId(participantId).participationStatus(ParticipationStatus.IN_PROGRESS)
                .answeredQuestionIds(new HashSet<>())
                .answers(new ArrayList<>()).build();
    }

    public boolean canceled() {
        return participationStatus != null && participationStatus == ParticipationStatus.CANCELED;
    }


    private static void checkSurveyCanBeParticipatedIn(Survey survey) {
        Set<String> failureMessages = new HashSet<>();

        if (survey.canBeParticipatedInIfNotCloseSurvey(failureMessages))
            return;


        StringJoiner message = new StringJoiner(FailureMessages.MESSAGES_DELIMETER);
        failureMessages.forEach(message::add);
        throw new ParticipationDomainException(message.toString());

    }


    public UserId getUserId() {
        return userId;
    }

    public Survey getSurvey() {
        return survey;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public ParticipationStatus getParticipationStatus() {
        return participationStatus;
    }

    public Set<QuestionId> getAnsweredQuestionIds() {
        return answeredQuestionIds;
    }


    public static Builder builder() {
        return new Builder();
    }

    public void complete() {
        if (!inProgress())
            throw new ParticipationDomainException("Participation is not in correct status to complete.");

        Map<QuestionId, Answer> questionIdAnswerMap = new HashMap<>();
        for (Answer answer : answers) {
            questionIdAnswerMap.put(answer.getQuestionId(), answer);
        }


        System.err.println(this);


        try {
            boolean canCompleted = survey.validateParticipationCompletion(questionIdAnswerMap);
            if (!canCompleted) {
                cancelParticipation();
                throw new ParticipationDomainException("survey is closed!");
            } else {
                participationStatus = ParticipationStatus.COMPLETED;
                survey.updateLogicData(questionIdAnswerMap);
            }
        } catch (QuotaEnabledException quotaEnabledException) {
            cancelParticipation();
            throw quotaEnabledException;
        }


    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public static final class Builder {
        private ParticipationId id;
        private UserId userId;
        private Survey survey;
        private ParticipationStatus participationStatus;
        private List<Answer> answers;
        private Set<QuestionId> answeredQuestionIds;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonProperty("id")
        public Builder participationId(ParticipationId val) {
            id = val;
            return this;
        }

        @JsonProperty("userId")
        public Builder userId(UserId val) {
            userId = val;
            return this;
        }

        @JsonProperty("survey")
        public Builder survey(Survey val) {
            survey = val;
            return this;
        }

        @JsonProperty("participationStatus")
        public Builder participationStatus(ParticipationStatus val) {
            participationStatus = val;
            return this;
        }

        @JsonProperty("answers")
        public Builder answers(List<Answer> val) {
            answers = val;
            return this;
        }

        @JsonProperty("answeredQuestionIds")
        public Builder answeredQuestionIds(Set<QuestionId> val) {
            answeredQuestionIds = val;
            return this;
        }

        public Participation build() {
            return new Participation(this);
        }
    }


    @Override
    public String toString() {
        return "Participation{" +
                "userId=" + userId +
                ", survey=" + survey +
                ", participationStatus=" + participationStatus +
                ", answers=" + answers +
                ", answeredQuestionIds=" + answeredQuestionIds +
                '}';
    }
}
