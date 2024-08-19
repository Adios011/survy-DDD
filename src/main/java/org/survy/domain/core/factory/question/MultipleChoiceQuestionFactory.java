package org.survy.domain.core.factory.question;

import org.survy.domain.applicationservice.dto.create.OptionAddRequest;
import org.survy.domain.applicationservice.dto.create.QuestionAddRequest;
import org.survy.domain.core.entity.question.MultipleChoiceQuestion;
import org.survy.domain.core.entity.question.Question;
import org.survy.domain.core.valueObject.Option;

import java.util.List;
import java.util.stream.Collectors;

public class MultipleChoiceQuestionFactory extends AbstractQuestionFactory {

    @Override
    public Question create(QuestionAddRequest request) {
        return new MultipleChoiceQuestion(request.getQuestionText(),
                request.getPage(),
                request.getOrder(),
                optionAddRequestToOption(request.getOptions()));


    }

    private List<Option> optionAddRequestToOption(List<OptionAddRequest> optionAddRequests) {
        return optionAddRequests.stream()
                .map(request ->
                        new Option(request.getLabel(), request.getOrder()))
                .collect(Collectors.toList());
    }
}
