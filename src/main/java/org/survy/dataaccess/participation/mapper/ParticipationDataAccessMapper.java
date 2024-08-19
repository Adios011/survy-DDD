package org.survy.dataaccess.participation.mapper;

import org.springframework.stereotype.Component;
import org.survy.dataaccess.participation.entity.AnswerEntity;
import org.survy.dataaccess.participation.entity.ParticipationEntity;
import org.survy.dataaccess.survey.mapper.SurveyDataAccessMapper;
import org.survy.domain.core.entity.Participation;
import org.survy.domain.core.entity.question.Answer;
import org.survy.domain.core.valueObject.BaseId;
import org.survy.domain.core.valueObject.ParticipationId;
import org.survy.domain.core.valueObject.QuestionId;
import org.survy.domain.core.valueObject.UserId;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ParticipationDataAccessMapper {

    private final SurveyDataAccessMapper surveyDataAccessMapper;

    public ParticipationDataAccessMapper(SurveyDataAccessMapper surveyDataAccessMapper) {
        this.surveyDataAccessMapper = surveyDataAccessMapper;
    }

    public ParticipationEntity toEntity(Participation participation) {
        ParticipationEntity to = new ParticipationEntity();
        to.setId(participation.getId().getValue());
        to.setParticipationStatus(participation.getParticipationStatus());
        to.setUserId(participation.getUserId().getValue());
        to.setAnsweredQuestionIds(participation.getAnsweredQuestionIds().stream().map(BaseId::getValue).collect(Collectors.toSet()));
        to.setSurvey(surveyDataAccessMapper.toEntity(participation.getSurvey()));
        to.setAnswers(toAnswerEntities(participation.getAnswers()));
        return to;
    }


    public List<AnswerEntity> toAnswerEntities(List<Answer> answerList) {
        return answerList.stream().map(answer ->
                        new AnswerEntity(answer.getId(), answer.getAnswerText(), answer.getQuestionId().getValue()))
                .collect(Collectors.toList());
    }

    public List<Answer> toAnswers(Set<AnswerEntity> answerEntities) {
        return answerEntities.stream().map(answerEntity ->
                        Answer.builder()
                                .id(answerEntity.getId())
                                .answerText(answerEntity.getAnswerText())
                                .questionId(new QuestionId(answerEntity.getQuestionId()))
                                .build()

                ).collect(Collectors.toList());
//                        new Answer(answerEntity.getId(), answerEntity.getAnswerText(), new QuestionId(answerEntity.getQuestionId())))



    }

    public Participation toParticipation(ParticipationEntity participationEntity) {
        return Participation.builder()
                .participationId(new ParticipationId(participationEntity.getId()))
                .userId(new UserId(participationEntity.getUserId()))
                .participationStatus(participationEntity.getParticipationStatus())
                .answeredQuestionIds(participationEntity.getAnsweredQuestionIds().stream().map(QuestionId::new).collect(Collectors.toSet()))
                .build();
    }

    public Participation toParticipationWithSurveyAndAnswers(ParticipationEntity jpaParticipation) {
        System.out.println("*********MAPLERKENNN*****");
        return Participation.builder()
                .participationId(new ParticipationId(jpaParticipation.getId()))
                .userId(new UserId(jpaParticipation.getUserId()))
                .participationStatus(jpaParticipation.getParticipationStatus())
                .answeredQuestionIds(jpaParticipation.getAnsweredQuestionIds().stream().map(QuestionId::new).collect(Collectors.toSet()))
//                TODO : think about this only difference from above
                .survey(surveyDataAccessMapper.toSurveyWithAllAssociations(jpaParticipation.getSurvey()))
                .answers(toAnswers(jpaParticipation.getAnswers()))
                .build();


    }


    public void copyFromDomain(Participation from, ParticipationEntity to) {
        to.setId(from.getId().getValue());
        to.setAnswers(toAnswerEntities(from.getAnswers()));
        to.setParticipationStatus(from.getParticipationStatus());
        to.setAnsweredQuestionIds(from.getAnsweredQuestionIds().stream().map(QuestionId::getValue).collect(Collectors.toSet()));
        to.setUserId(from.getUserId().getValue());

        //TODO: delete this code
        surveyDataAccessMapper.copyFromDomain(to.getSurvey(), from.getSurvey());
    }
}
