package org.survy.domain.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.transaction.annotation.Transactional;
import org.survy.SpringApplicationContext;
import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.core.entity.logic.ParticipationLogic;
import org.survy.domain.core.entity.question.Answer;
import org.survy.domain.core.event.DomainEventPublisher;
import org.survy.domain.core.event.SurveyClosedEvent;
import org.survy.domain.core.exception.*;
import org.survy.domain.core.exception.messages.FailureMessages;
import org.survy.domain.core.exception.messages.InputRules;
import org.survy.domain.core.valueObject.*;
import org.survy.domain.core.entity.question.Question;


import java.util.*;
import java.util.stream.Collectors;


@JsonDeserialize(builder = Survey.Builder.class)

public class Survey extends AggregateRoot<SurveyId> {

    private SurveyTitle title;
    private SurveyDescription description;
    private CloseDate closeDate;
    private String category;
    private int numberOfPages;
    private SurveyStatus surveyStatus;
    private List<UserId> creators;


    private List<Question> questions;
    private List<ParticipationLogic> participationLogics;


    public Survey() {
    }

    public ParticipationLogic addParticipationLogic(ParticipationLogic logic) {

        if (participationLogics == null)
            this.participationLogics = new ArrayList<>();

        validateToAddQuestion();
        logic.initialize(this);
        participationLogics.add(logic);
        return logic;
    }


    public Question findQuestionById(QuestionId questionId) {
        return questions.stream()
                .filter(question -> question.getId().equals(questionId))
                .findFirst()
                .orElse(null);
    }


    public void filterQuestions(Set<QuestionId> answeredQuestionIds) {
        this.questions = questions.stream().filter(question -> !answeredQuestionIds.contains(question.getId())).collect(Collectors.toList());
    }

    @Transactional
    public boolean canBeParticipatedInIfNotCloseSurvey(Set<String> failureMessages) {
        if (!isOpen()) {
            failureMessages.add(FailureMessages.SURVEY_CLOSED);
            return false;
        }


        boolean canBeParticipatedIn = true;
        for (ParticipationLogic participationLogic : participationLogics) {
            canBeParticipatedIn = canBeParticipatedIn && participationLogic.canBeParticipatedInSurvey(failureMessages);
        }


        if (failureMessages.contains(FailureMessages.ALL_QUOTAS_REACHED)) {
            closeSurvey();
            DomainEventPublisher.getInstance().publish(new SurveyClosedEvent(getId() , FailureMessages.ALL_QUOTAS_REACHED));
        }


        return canBeParticipatedIn;

    }


    private void closeSurvey() {
        if (initialized())
            surveyStatus = SurveyStatus.CLOSED;

    }


    /**
     * It validates and adds question to survey.
     *
     * @param question to be added
     * @return question added
     * @throws QuestionNotAddedException if survey or question is not in correct status
     */
    public Question addQuestion(Question question) throws QuestionNotAddedException {
        if (this.questions == null)
            this.questions = new ArrayList<>();


        try {
            validateToAddQuestion();
            question.initQuestion(this.getId());
            questions.add(question);
        } catch (SurveyInitializationException | QuestionInitializationException exception) {
            throw new QuestionNotAddedException(exception.getMessage(), exception);
        }
        return question;
    }

    private void validateToAddQuestion() {
        if (!initialized())
            throw new SurveyInitializationException(FailureMessages.SURVEY_INITIAL_STATUS);
    }


    public void completeCreationProcess() {
        validateCompletion();
        surveyStatus = SurveyStatus.OPEN;
    }

    private void validateCompletion() {
        if (isClosed())
            throw new SurveyDomainException("survey is closed!");

        if (isOpen())
            throw new SurveyDomainException("survey is already open!");

        validateInitialProperties();

        if (questions == null || questions.isEmpty())
            throw new SurveyDomainException("survey must contains at least one question");

    }


    public boolean isClosed() {
        return surveyStatus == SurveyStatus.CLOSED;
    }

    public boolean isOpen() {
        return surveyStatus == SurveyStatus.OPEN;
    }


    //*********************

    /**
     * It initializes Survey with UNCONFIGURED status.
     *
     * @throws SurveyInitializationException if not in appropriate status or not have valid properties for initialization.
     */
    public void initializeSurvey() throws SurveyInitializationException {
        validateSurvey();

        this.surveyStatus = SurveyStatus.UNCONFIGURED;
        setId(new SurveyId(UUID.randomUUID()));
        questions = new ArrayList<>();
        participationLogics = new ArrayList<>();
    }

    private void validateSurvey() {
        validateInitialStatus();
        validateInitialProperties();
    }

    private void validateInitialStatus() {
        if (initialized())
            throw new SurveyInitializationException(FailureMessages.SURVEY_INITIAL_STATUS);
    }

    private void validateInitialProperties() {
        List<String> failureMessages = new ArrayList<>();

        if (title == null || !title.valid())
            failureMessages.add(InputRules.SURVEY_TITLE);

        if (closeDate == null || closeDate.inPastTime())
            failureMessages.add(InputRules.SURVEY_CLOSE_DATE);

        if (description == null || !description.valid())
            failureMessages.add(InputRules.SURVEY_DESCRIPTION);

        if (creators == null || creators.isEmpty() || creators.get(0) == null || creators.get(0).getValue() == null)
            failureMessages.add(InputRules.SURVEY_CREATOR);


        if (!failureMessages.isEmpty()) {
            StringJoiner joiner = new StringJoiner("--");
            failureMessages.forEach(joiner::add);
            throw new SurveyInitializationException(joiner.toString());
        }

    }

    private boolean initialized() {
        return (getId() != null || surveyStatus != null);
    }

    public boolean unconfigured() {
        return surveyStatus != null && surveyStatus == SurveyStatus.UNCONFIGURED;
    }


    private Survey(Builder builder) {
        super.setId(builder.id);
        title = builder.title;
        description = builder.description;
        closeDate = builder.closeDate;
        category = builder.category;
        numberOfPages = builder.numberOfPages;
        surveyStatus = builder.surveyStatus;
        creators = builder.creators;
        questions = builder.questions;
        participationLogics = builder.participationLogics;
    }

    public static Builder builder() {
        return new Builder();
    }


    public SurveyTitle getTitle() {
        return title;
    }

    public SurveyDescription getDescription() {
        return description;
    }

    public CloseDate getCloseDate() {
        return closeDate;
    }

    public SurveyStatus getSurveyStatus() {
        return surveyStatus;
    }

    public String getCategory() {
        return category;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<UserId> getCreators() {
        return creators;
    }

    public List<ParticipationLogic> getParticipationLogics() {
        return participationLogics;
    }


    @Override
    public String toString() {
        return "Survey{" +
                "title=" + title +
                ", description=" + description +
                ", closeDate=" + closeDate +
                ", category='" + category + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", surveyStatus=" + surveyStatus +
                ", questions=" + questions +
                ", creators=" + creators +
                ", participationLogics=" + participationLogics +
                '}';
    }

    public Answer answerQuestion(QuestionId questionId, AnswerRequest answerRequest) {
        Question questionToBeAnswered = findQuestionById(questionId);
        if (questionToBeAnswered == null)
            throw new SurveyDomainException("Survey does NOT have such question " + questionId);

        if (!checkCanBeAnswered(questionId, answerRequest))
            throw new ParticipationDomainException("Survey cannot be participated in!");

        return questionToBeAnswered.answer(answerRequest);


    }

    private boolean checkCanBeAnswered(QuestionId questionId, AnswerRequest answerRequest) {
        if (!isOpen())
            return false;

        boolean canBeAnswered = true;

        for (ParticipationLogic participationLogic : participationLogics) {
            canBeAnswered = canBeAnswered && participationLogic.canQuestionBeAnswered(questionId, answerRequest);
        }

        return canBeAnswered;


    }

    public boolean validateParticipationCompletion(Map<QuestionId, Answer> questionIdAnswerMap) throws QuotaEnabledException {
        if (!isOpen())
            throw new ParticipationDomainException("survey is closed!");

        System.err.println(this);

        boolean canCompleted = true;
        for (ParticipationLogic participationLogic : participationLogics) {
            canCompleted = canCompleted && participationLogic.canParticipationBeCompleted(questionIdAnswerMap);
        }

        return canCompleted;

    }

    public void updateLogicData(Map<QuestionId, Answer> questionIdAnswerMap) {
        for (ParticipationLogic participationLogic : participationLogics) {
            participationLogic.updateLogicData(questionIdAnswerMap);
        }
    }

    public void close() {
        surveyStatus = SurveyStatus.CLOSED;
    }

    @JsonIgnoreProperties({"closed", "open", "initialized"})
    public static final class Builder {
        private SurveyId id;

        private SurveyTitle title;
        private SurveyDescription description;
        private CloseDate closeDate;
        private SurveyStatus surveyStatus;
        private List<Question> questions;
        private String category;
        private int numberOfPages;
        private List<UserId> creators;
        private List<ParticipationLogic> participationLogics;

        public Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        @JsonProperty("id")
        public Builder id(SurveyId surveyId) {
            this.id = surveyId;
            return this;
        }

        @JsonProperty("title")
        public Builder title(SurveyTitle val) {
            title = val;
            return this;
        }

        @JsonProperty("description")
        public Builder description(SurveyDescription val) {
            description = val;
            return this;
        }

        @JsonProperty("closeDate")
        public Builder closeDate(CloseDate val) {
            closeDate = val;
            return this;
        }

        @JsonProperty("surveyStatus")
        public Builder surveyStatus(SurveyStatus val) {
            surveyStatus = val;
            return this;
        }

        @JsonProperty("questions")
        public Builder questions(List<Question> val) {
            questions = val;
            return this;
        }

        @JsonProperty("category")
        public Builder category(String val) {
            category = val;
            return this;
        }

        @JsonProperty("numberOfPages")
        public Builder numberOfPages(int val) {
            numberOfPages = val;
            return this;
        }

        @JsonProperty("creators")
        public Builder creators(List<UserId> val) {
            creators = val;
            return this;
        }

        @JsonProperty("participationLogics")
        public Builder participationLogics(List<ParticipationLogic> val) {
            participationLogics = val;
            return this;
        }

        public Survey build() {
            return new Survey(this);
        }


    }


}
