package org.survy.domain.core.domainService.impl;

import org.survy.domain.core.domainService.SurveyDomainService;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.entity.logic.ParticipationLogic;
import org.survy.domain.core.entity.question.Question;
import org.survy.domain.core.exception.QuestionNotAddedException;
import org.survy.domain.core.exception.SurveyInitializationException;

public class SurveyDomainServiceImpl implements SurveyDomainService {


    @Override
    public Survey startSurveyCreationProcess(Survey survey) throws SurveyInitializationException {
        survey.initializeSurvey();
        return survey;
    }

    @Override
    public Survey addQuestionToSurvey(Question question, Survey survey) throws QuestionNotAddedException {
        survey.addQuestion(question);
        return survey;
    }

    @Override
    public Survey addParticipationLogicToSurvey(ParticipationLogic logic, Survey survey) {
        survey.addParticipationLogic(logic);
        return survey;
    }

    @Override
    public Survey completeSurveyCreationProcess(Survey survey) {
        survey.completeCreationProcess();
        return survey;
    }


}
