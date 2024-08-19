package org.survy.domain.core.domainService;

import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.entity.logic.ParticipationLogic;
import org.survy.domain.core.entity.question.Question;
import org.survy.domain.core.exception.QuestionNotAddedException;
import org.survy.domain.core.exception.SurveyInitializationException;

public interface SurveyDomainService {

    /**
     * It returns Survey with id and UNCONFIGURED status.
     * @param survey  to be initialized
     * @return survey - initialized
     * @throws SurveyInitializationException if not in appropriate status or not have valid properties for initialization
     */
     Survey startSurveyCreationProcess(Survey survey);

    /**
     * It validates, initializes and adds question to survey.
     * @param question to be added
     * @param survey survey to which question be added
     * @return
     * @throws QuestionNotAddedException If survey is not in correct status or question is invalid.
     */
    Survey addQuestionToSurvey(Question question , Survey survey) throws QuestionNotAddedException;

    Survey addParticipationLogicToSurvey(ParticipationLogic logic, Survey survey);

    Survey completeSurveyCreationProcess(Survey survey);
}
