package org.survy.domain.core.factory.question;

import org.survy.domain.applicationservice.dto.create.QuestionAddRequest;
import org.survy.domain.core.entity.question.Question;

public abstract class AbstractQuestionFactory {

    public abstract Question create(QuestionAddRequest request);
}
