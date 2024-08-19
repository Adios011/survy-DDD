package org.survy.domain.core.entity.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.entity.question.Answer;
import org.survy.domain.core.entity.question.Question;
import org.survy.domain.core.entity.question.QuotaApplicableQuestion;
import org.survy.domain.core.exception.ParticipationLogicInitializationException;
import org.survy.domain.core.exception.QuestionDomainException;
import org.survy.domain.core.exception.SurveyDomainException;
import org.survy.domain.core.exception.messages.FailureMessages;
import org.survy.domain.core.exception.messages.InputRules;
import org.survy.domain.core.valueObject.QuestionId;
import org.survy.domain.core.valueObject.QuotaAction;
import org.survy.domain.core.valueObject.SurveyId;

import java.util.*;

@JsonDeserialize(builder = SimpleQuotaLogic.Builder.class)
public class SimpleQuotaLogic extends ParticipationLogic {

    public static final int CURRENT_INITIAL_VALUE = 0;


    private int quota;
    private QuestionId questionId;
    private Map<String, Integer> optionCurrentMap;
    private QuotaAction quotaAction;


    private SimpleQuotaLogic(Builder builder) {
        setId(builder.id);
        surveyId = builder.surveyId;
        quota = builder.quota;
        questionId = builder.questionId;
        optionCurrentMap = builder.optionCurrentMap;
        quotaAction = builder.quotaAction;
    }

    @Override
    public void validateWithSurvey(Survey survey) {
        Question question = checkSurveyContainsQuestion(survey);
        checkQuotaApplicableQuestion(question);
        List<String> failureMessages = new ArrayList<>();
        checkProperties(failureMessages);

        if (!failureMessages.isEmpty()) {
            StringJoiner message = new StringJoiner(FailureMessages.MESSAGES_DELIMETER);
            failureMessages.forEach(message::add);
            throw new ParticipationLogicInitializationException(message.toString());
        }


    }

    private void checkProperties(List<String> failureMessages) {

        if (questionId == null || questionId.getValue() == null)
            failureMessages.add(InputRules.QUOTA_MUST_CONTAIN_QUESTION);

        if (quota <= 0)
            failureMessages.add(InputRules.QUOTA_VALUE);

        if (optionCurrentMap == null || optionCurrentMap.isEmpty())
            failureMessages.add(InputRules.QUOTA_OPTION);
    }

    private void checkQuotaApplicableQuestion(Question question) {
        if (question instanceof QuotaApplicableQuestion quotaApplicableQuestion) {
            for (String optionLabel : optionCurrentMap.keySet())
                if (!quotaApplicableQuestion.containsOptionByLabel(optionLabel))
                    throw new ParticipationLogicInitializationException(FailureMessages.NO_OPTION_FOUND + optionLabel);

        } else
            throw new ParticipationLogicInitializationException(FailureMessages.QUOTA_NOT_APPLICABLE + questionId.getValue());


    }

    private Question checkSurveyContainsQuestion(Survey survey) {
        Question question = survey.findQuestionById(questionId);
        if (question == null)
            throw new ParticipationLogicInitializationException(FailureMessages.NO_QUESTION_FOUND + questionId.getValue());

        return question;
    }

    @Override
    public boolean canBeParticipatedInSurvey(Set<String> failureMessages) {
        if (allQuotasReached()) {
            failureMessages.add(FailureMessages.ALL_QUOTAS_REACHED);
            return false;
        } else
            return true;


    }

    @Override
    public boolean canQuestionBeAnswered(QuestionId questionId, AnswerRequest answer) {
        if (allQuotasReached())
            return false;

        if (!this.questionId.equals(questionId))
            return true;

        String choiceOptionLabel = answer.getChoiceOptionLabel();
        if (!optionCurrentMap.containsKey(choiceOptionLabel))
            return true;


        int current = optionCurrentMap.get(choiceOptionLabel);
        boolean result = current < quota;

        System.out.println("RESULT ----> " + result);

        if (result)
            return true;
        else {
            if (quotaAction != null)
                quotaAction.act();
            return false;
        }
    }

    @Override
    public boolean canParticipationBeCompleted(Map<QuestionId, Answer> questionIdAnswerMap) {
        if (allQuotasReached())
            return false;

        Answer answer = questionIdAnswerMap.get(questionId);
        if (answer == null)
            return true;

        AnswerRequest request = new AnswerRequest();
        request.setChoiceOptionLabel(answer.getAnswerText());
        return canQuestionBeAnswered(questionId, request);


    }

    @Override
    public void updateLogicData(Map<QuestionId, Answer> questionIdAnswerMap) {

        Answer answer = questionIdAnswerMap.get(this.questionId);
        if (answer == null)
            return;

        String choiceOptionLabel = answer.getAnswerText();

        int current = optionCurrentMap.get(choiceOptionLabel);
        current++;
        this.optionCurrentMap.replace(choiceOptionLabel, current);

    }


    private boolean allQuotasReached() {
        boolean flag = false;
        for (String option : optionCurrentMap.keySet()) {
            int current = optionCurrentMap.get(option);
            flag = flag || (current < quota);
        }

        return !flag;
    }


    public int getQuota() {
        return quota;
    }

    public QuestionId getQuestionId() {
        return questionId;
    }

    public Map<String, Integer> getOptionCurrentMap() {
        return optionCurrentMap;
    }

    public QuotaAction getQuotaAction() {
        return quotaAction;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private UUID id;
        private SurveyId surveyId;
        private int quota;
        private QuestionId questionId;
        private Map<String, Integer> optionCurrentMap;
        private QuotaAction quotaAction;

        private Builder() {
        }

        @JsonProperty("id")
        public Builder id(UUID val) {
            id = val;
            return this;
        }

        @JsonProperty("quotaAction")
        public Builder quotaAction(QuotaAction val) {
            quotaAction = val;
            return this;
        }

        @JsonProperty("surveyId")
        public Builder surveyId(SurveyId val) {
            surveyId = val;
            return this;
        }

        @JsonProperty("quota")
        public Builder quota(int val) {
            quota = val;
            return this;
        }

        @JsonProperty("questionId")
        public Builder questionId(QuestionId val) {
            questionId = val;
            return this;
        }

        @JsonProperty("optionCurrentMap")
        public Builder optionCurrentMap(Map<String, Integer> val) {
            optionCurrentMap = val;
            return this;
        }

        public SimpleQuotaLogic build() {
            return new SimpleQuotaLogic(this);
        }
    }
}
