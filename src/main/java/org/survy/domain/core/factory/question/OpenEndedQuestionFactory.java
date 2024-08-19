package org.survy.domain.core.factory.question;

import org.survy.domain.applicationservice.dto.create.QuestionAddRequest;
import org.survy.domain.core.entity.question.OpenEndedQuestion;

public class OpenEndedQuestionFactory extends AbstractQuestionFactory {

    @Override
    public OpenEndedQuestion create(QuestionAddRequest request) {
        return new OpenEndedQuestion(request.getQuestionText(),
                request.getPage(),
                request.getOrder());
    }
}
