package org.survy.domain.core.entity.question;

import org.survy.domain.applicationservice.dto.participation.AnswerRequest;
import org.survy.domain.core.entity.BaseEntity;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.exception.QuestionDomainException;
import org.survy.domain.core.exception.QuestionInitializationException;
import org.survy.domain.core.exception.messages.FailureMessages;
import org.survy.domain.core.exception.messages.InputRules;
import org.survy.domain.core.valueObject.SurveyId;
import org.survy.domain.core.valueObject.QuestionId;

import java.util.*;

public abstract class Question extends BaseEntity<QuestionId> {

    protected final String questionText;
    protected final int page;
    protected final int order;
    protected SurveyId surveyId;


    public abstract Answer answer(AnswerRequest answerRequest);

    public abstract String getType();


    public Question(String questionText, int page, int order) {
        this.questionText = questionText;
        this.page = page;
        this.order = order;
    }

    public void initQuestion(SurveyId surveyId) throws QuestionInitializationException {
        List<String> failureMessages = new ArrayList<>();

        validateQuestion(failureMessages);

        if(!failureMessages.isEmpty()){
            StringJoiner message = new StringJoiner(FailureMessages.MESSAGES_DELIMETER);
            failureMessages.forEach(message::add);

            throw new QuestionInitializationException(message.toString());
        }


        super.setId(new QuestionId(UUID.randomUUID()));
        this.surveyId = surveyId;
    }


    public void validateQuestion(List<String> failureMessages) {


        if (questionText == null || questionText.isEmpty())
            failureMessages.add(InputRules.QUESTION_TEXT);

        if ((page < 0 || page > 10))
            failureMessages.add(InputRules.QUESTION_PAGE);

        if (order < 0)
            failureMessages.add(InputRules.QUESTION_ORDER);

        validate(failureMessages);
    }

    protected abstract void validate(List<String> failureMessages);




    public SurveyId getSurveyId() {
        return surveyId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getPage() {
        return page;
    }

    public int getOrder() {
        return order;
    }
}
