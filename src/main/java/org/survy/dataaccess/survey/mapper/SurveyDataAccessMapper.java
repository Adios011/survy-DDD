package org.survy.dataaccess.survey.mapper;

import org.springframework.stereotype.Component;
import org.survy.dataaccess.question.mapper.QuestionDataAccessMapper;
import org.survy.dataaccess.survey.entity.SurveyEntity;
import org.survy.domain.core.entity.Survey;
import org.survy.domain.core.valueObject.*;

import java.util.stream.Collectors;

@Component
public class SurveyDataAccessMapper {

    private final QuestionDataAccessMapper questionDataAccessMapper;
    private final ParticipationLogicDataAccessMapper participationLogicDataAccessMapper;

    public SurveyDataAccessMapper(QuestionDataAccessMapper questionDataAccessMapper,
                                  ParticipationLogicDataAccessMapper participationLogicDataAccessMapper) {
        this.questionDataAccessMapper = questionDataAccessMapper;
        this.participationLogicDataAccessMapper = participationLogicDataAccessMapper;
    }

    public SurveyEntity toEntity(Survey from) {

        if (from == null)
            return null;

        SurveyEntity to = new SurveyEntity();
        to.setId(from.getId().getValue());
        to.setCreators(from.getCreators().stream().map(BaseId::getValue).collect(Collectors.toList()));
        to.setTitle(from.getTitle().getTitle());
        to.setDescription(from.getDescription().getDescription());
        to.setCloseDate(from.getCloseDate().getDate());
        to.setCategory(from.getCategory());
        to.setSurveyStatus(from.getSurveyStatus());
        to.setQuestions(questionDataAccessMapper.toEntities(from.getQuestions()));
        to.setParticipationLogics(participationLogicDataAccessMapper.toEntities(from.getParticipationLogics()));

        return to;
    }

    public Survey toSurveyWithQuestions(SurveyEntity jpaEntity) {
        return Survey.builder()
                .id(new SurveyId(jpaEntity.getId()))
                .creators(jpaEntity.getCreators().stream().map(UserId::new).collect(Collectors.toList()))
                .surveyStatus(jpaEntity.getSurveyStatus())
                .closeDate(new CloseDate(jpaEntity.getCloseDate()))
                .description(new SurveyDescription(jpaEntity.getDescription()))
                .title(new SurveyTitle(jpaEntity.getTitle()))
                .category(jpaEntity.getCategory())
                .questions(questionDataAccessMapper.toQuestions(jpaEntity.getQuestions()))
                .build();
    }

    public void copyFromDomain(SurveyEntity to, Survey from) {
        to.setId(from.getId().getValue());
        to.setCategory(from.getCategory());
        to.setDescription(from.getDescription().getDescription());
        to.setSurveyStatus(from.getSurveyStatus());
        to.setTitle(from.getTitle().getTitle());
        to.setCloseDate(from.getCloseDate().getDate());
        to.setCreators(from.getCreators().stream().map(BaseId::getValue).collect(Collectors.toList()));

        if (from.getQuestions() != null)
            to.setQuestions(questionDataAccessMapper.toEntities(from.getQuestions()));

        if (from.getParticipationLogics() != null)
            to.setParticipationLogics(participationLogicDataAccessMapper.toEntities(from.getParticipationLogics()));


    }

    public Survey toSurveyWithAllAssociations(SurveyEntity jpaEntity) {
        return Survey.builder()
                .id(new SurveyId(jpaEntity.getId()))
                .creators(jpaEntity.getCreators().stream().map(UserId::new).collect(Collectors.toList()))
                .surveyStatus(jpaEntity.getSurveyStatus())
                .closeDate(new CloseDate(jpaEntity.getCloseDate()))
                .description(new SurveyDescription(jpaEntity.getDescription()))
                .title(new SurveyTitle(jpaEntity.getTitle()))
                .category(jpaEntity.getCategory())
                .questions(questionDataAccessMapper.toQuestions(jpaEntity.getQuestions()))
                .participationLogics(participationLogicDataAccessMapper.toDomainList(jpaEntity.getParticipationLogics()))
                .build();
    }
}
