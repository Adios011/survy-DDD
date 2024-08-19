package org.survy.domain.applicationservice.mapper;

import org.springframework.stereotype.Component;
import org.survy.domain.applicationservice.dto.create.QuestionAddRequest;
import org.survy.domain.applicationservice.dto.create.QuestionAddResponse;
import org.survy.domain.applicationservice.dto.participation.OptionResponse;
import org.survy.domain.applicationservice.dto.participation.QuestionResponse;
import org.survy.domain.core.entity.question.MultipleChoiceQuestion;
import org.survy.domain.core.entity.question.OpenEndedQuestion;
import org.survy.domain.core.entity.question.Question;
import org.survy.domain.core.entity.question.RatingScaleQuestion;
import org.survy.domain.core.exception.QuestionDomainException;
import org.survy.domain.core.factory.question.MultipleChoiceQuestionFactory;
import org.survy.domain.core.factory.question.OpenEndedQuestionFactory;
import org.survy.domain.core.factory.question.RatingScaleQuestionFactory;
import org.survy.domain.core.valueObject.QuestionTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionDataMapper {

    public Question questionAddRequestToQuestion(QuestionAddRequest request) {
        String type = request.getType();

        if (type.equalsIgnoreCase(QuestionTypes.MULTIPLE_CHOICE.name()))
            return new MultipleChoiceQuestionFactory().create(request);
        else if (type.equalsIgnoreCase(QuestionTypes.OPEN_ENDED.name()))
            return new OpenEndedQuestionFactory().create(request);
        else if (type.equalsIgnoreCase(QuestionTypes.RATING_SCALE.name()))
            return new RatingScaleQuestionFactory().create(request);
        else
            throw new QuestionDomainException("unknown question type: " + type);
    }

    public QuestionAddResponse questionToQuestionAddResponse(Question questionAdded) {
        return QuestionAddResponse.builder()
                .questionId(questionAdded.getId().getValue())
                .surveyId(questionAdded.getSurveyId().getValue())
                .questionText(questionAdded.getQuestionText())
                .build();
    }


    public List<QuestionResponse> toQuestionResponses(List<Question> questions) {
        List<QuestionResponse> questionResponses = new ArrayList<>();

        for (Question question : questions) {
            if (question instanceof MultipleChoiceQuestion multipleChoiceQuestion)
                questionResponses.add(toQuestionResponse(multipleChoiceQuestion));
            else if (question instanceof RatingScaleQuestion ratingScaleQuestion)
                questionResponses.add(toQuestionResponse(ratingScaleQuestion));
            else if (question instanceof OpenEndedQuestion openEndedQuestion)
                questionResponses.add(toQuestionResponse(openEndedQuestion));
            else
                throw new QuestionDomainException("Unknown subtype " + question.getClass().getName());
        }

        return questionResponses;
    }

    public QuestionResponse toQuestionResponse(MultipleChoiceQuestion question) {
        return QuestionResponse.builder()
                .questionId(question.getId().getValue())
                .questionText(question.getQuestionText())
                .type(question.getType())
                .page(question.getPage())
                .order(question.getOrder())
                .options(question.getOptions().stream()
                        .map(option -> new OptionResponse(option.getLabel(), option.getOrder())).collect(Collectors.toList()))
                .build();
    }

    public QuestionResponse toQuestionResponse(RatingScaleQuestion question) {
        return QuestionResponse.builder()
                .questionId(question.getId().getValue())
                .questionText(question.getQuestionText())
                .type(question.getType())
                .page(question.getPage())
                .order(question.getOrder())
                .options(question.getWeightedOptions().stream()
                        .map(option -> new OptionResponse(option.getLabel(), option.getOrder())).collect(Collectors.toList()))
                .build();
    }

    public QuestionResponse toQuestionResponse(OpenEndedQuestion question) {
        return QuestionResponse.builder()
                .questionId(question.getId().getValue())
                .questionText(question.getQuestionText())
                .type(question.getType())
                .page(question.getPage())
                .order(question.getOrder())
                .build();
    }


}
