package org.survy.dataaccess.question.mapper;

import org.springframework.stereotype.Component;
import org.survy.dataaccess.question.entity.MultipleChoiceQuestionEntity;
import org.survy.dataaccess.question.entity.OpenEndedQuestionEntity;
import org.survy.dataaccess.question.entity.QuestionEntity;
import org.survy.dataaccess.question.entity.RatingScaleQuestionEntity;
import org.survy.domain.core.entity.question.MultipleChoiceQuestion;
import org.survy.domain.core.entity.question.OpenEndedQuestion;
import org.survy.domain.core.entity.question.Question;
import org.survy.domain.core.entity.question.RatingScaleQuestion;
import org.survy.domain.core.valueObject.QuestionId;
import org.survy.domain.core.valueObject.SurveyId;

import java.util.*;

@Component
public class QuestionDataAccessMapper {

    public Set<QuestionEntity> toEntities(List<Question> fromList) {
        if (fromList == null)
            return null;

        Set<QuestionEntity> entityList = new HashSet<>();

        for (Question from : fromList) {
            if (from == null)
                continue;

            if (from instanceof MultipleChoiceQuestion multipleChoiceQuestion)
                entityList.add(toMultipleChoiceQuestionEntity(multipleChoiceQuestion));
            else if (from instanceof RatingScaleQuestion ratingScaleQuestion)
                entityList.add(toRatingScaleQuestionEntity(ratingScaleQuestion));
            else if (from instanceof OpenEndedQuestion openEndedQuestion)
                entityList.add(toOpenEndedQuestionEntity(openEndedQuestion));
            else
                throw new RuntimeException("unknown subtype " + from.getClass().getName());
        }
        return entityList;
    }

    private void copyCommonAttributes(Question from, QuestionEntity to) {
        to.setId(from.getId().getValue());
        to.setQuestionText(from.getQuestionText());
        to.setPage(from.getPage());
        to.setPosition(from.getOrder());
    }


    private OpenEndedQuestionEntity toOpenEndedQuestionEntity(OpenEndedQuestion from) {
        if (from == null)
            return null;

        OpenEndedQuestionEntity to = new OpenEndedQuestionEntity();
        copyCommonAttributes(from, to);
        return to;
    }

    private RatingScaleQuestionEntity toRatingScaleQuestionEntity(RatingScaleQuestion from) {
        if (from == null)
            return null;

        RatingScaleQuestionEntity to = new RatingScaleQuestionEntity();
        copyCommonAttributes(from, to);
        to.setWeightedOptions(from.getWeightedOptions());
        return to;
    }

    public MultipleChoiceQuestionEntity toMultipleChoiceQuestionEntity(MultipleChoiceQuestion from) {
        if (from == null)
            return null;

        MultipleChoiceQuestionEntity to = new MultipleChoiceQuestionEntity();
        copyCommonAttributes(from, to);
        to.setOptions(from.getOptions());
        return to;
    }







    public List<Question> toQuestions(Set<QuestionEntity> questions) {
        List<Question> toList = new ArrayList<>();

        for (QuestionEntity from : questions) {
            if (from instanceof MultipleChoiceQuestionEntity multipleChoiceQuestionEntity)
                toList.add(toMultipleChoiceQuestion(multipleChoiceQuestionEntity));
            else if (from instanceof RatingScaleQuestionEntity ratingScaleQuestionEntity)
                toList.add(toRatingScaleQuestion(ratingScaleQuestionEntity));
            else if (from instanceof OpenEndedQuestionEntity openEndedQuestionEntity)
                toList.add(toOpenEndedQuestion(openEndedQuestionEntity));
            else
                throw new RuntimeException("unknown subtype " + from.getClass().getName());
        }
        return toList;

    }

    private MultipleChoiceQuestion toMultipleChoiceQuestion(MultipleChoiceQuestionEntity from) {
        return MultipleChoiceQuestion.builder()
                .id(new QuestionId(from.getId()))
                .questionText(from.getQuestionText())
                .page(from.getPage())
                .order(from.getPosition())
                .options(from.getOptions())
                .surveyId(new SurveyId(from.getSurvey().getId()))
                .build();
    }

    private RatingScaleQuestion toRatingScaleQuestion(RatingScaleQuestionEntity from) {
        return RatingScaleQuestion.builder()
                .id(new QuestionId(from.getId()))
                .questionText(from.getQuestionText())
                .page(from.getPage())
                .order(from.getPosition())
                .weightedOptions(from.getWeightedOptions())
                .surveyId(new SurveyId(from.getSurvey().getId()))
                .build();



    }

    private OpenEndedQuestion toOpenEndedQuestion(OpenEndedQuestionEntity from) {
        return OpenEndedQuestion.builder()
                .id(new QuestionId(from.getId()))
                .questionText(from.getQuestionText())
                .page(from.getPage())
                .order(from.getPosition())
                .surveyId(new SurveyId(from.getSurvey().getId()))
                .build();
    }

}
