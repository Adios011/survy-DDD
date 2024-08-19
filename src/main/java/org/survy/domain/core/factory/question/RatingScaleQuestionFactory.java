package org.survy.domain.core.factory.question;

import org.survy.domain.applicationservice.dto.create.OptionAddRequest;
import org.survy.domain.applicationservice.dto.create.QuestionAddRequest;
import org.survy.domain.core.entity.question.Question;
import org.survy.domain.core.entity.question.RatingScaleQuestion;
import org.survy.domain.core.valueObject.Option;
import org.survy.domain.core.valueObject.WeightedOption;

import java.util.List;
import java.util.stream.Collectors;

public class RatingScaleQuestionFactory extends AbstractQuestionFactory{

    @Override
    public RatingScaleQuestion create(QuestionAddRequest request) {
        return new RatingScaleQuestion(request.getQuestionText(),
                request.getPage() , request.getOrder(),
                optionAddRequestToOption(request.getOptions()));
        }


    private List<WeightedOption> optionAddRequestToOption(List<OptionAddRequest> optionAddRequests) {
        return optionAddRequests.stream()
                .map(request ->
                        new WeightedOption(request.getLabel(), request.getOrder() , request.getWeight()))
                .collect(Collectors.toList());
    }
}
