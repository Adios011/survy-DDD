package org.survy.domain.applicationservice.mapper;

import org.springframework.stereotype.Component;
import org.survy.domain.applicationservice.dto.create.SurveyCreationStartRequest;
import org.survy.domain.applicationservice.dto.create.SurveyCreationStartResponse;
import org.survy.domain.applicationservice.dto.participation.SurveyResponse;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.valueObject.CloseDate;
import org.survy.domain.core.valueObject.SurveyDescription;
import org.survy.domain.core.valueObject.SurveyTitle;
import org.survy.domain.core.valueObject.UserId;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class SurveyDataMapper {

    private final QuestionDataMapper questionDataMapper;

    public SurveyDataMapper(QuestionDataMapper questionDataMapper) {
        this.questionDataMapper = questionDataMapper;
    }

    public Survey surveyCreationStartRequestToSurvey(SurveyCreationStartRequest request){
        return Survey.builder()
                .creators(new ArrayList<>(Arrays.asList(new UserId(request.getCreatorId()))))
                .title(new SurveyTitle(request.getTitle()))
                .description(new SurveyDescription(request.getDescription()))
                .closeDate(new CloseDate(request.getCloseDate()))
                .build();
    }

    public SurveyCreationStartResponse surveyToSurveyCreationStartResponse(Survey survey){
        return  new SurveyCreationStartResponse(survey.getId().getValue());
    }

    public SurveyResponse toSurveyResponse(Survey survey) {
        return SurveyResponse.builder()
                .questions(questionDataMapper.toQuestionResponses(survey.getQuestions()))

                .build();

    }
}
