package org.survy.dataaccess.participation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.survy.dataaccess.survey.entity.SurveyEntity;
import org.survy.domain.core.entity.question.Answer;
import org.survy.domain.core.valueObject.ParticipationStatus;

import java.util.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participations")

public class ParticipationEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY , cascade = {CascadeType.MERGE , CascadeType.PERSIST})
    @JoinColumn(name = "surveys_id")
    private SurveyEntity survey;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus participationStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    private Set<UUID> answeredQuestionIds;

    @OneToMany(mappedBy = "participation", cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<AnswerEntity> answers;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationEntity that = (ParticipationEntity) o;
        return Objects.equals(id, that.id);
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public SurveyEntity getSurvey() {
        return survey;
    }

    public void setSurvey(SurveyEntity survey) {
        this.survey = survey;
    }

    public ParticipationStatus getParticipationStatus() {
        return participationStatus;
    }

    public void setParticipationStatus(ParticipationStatus participationStatus) {
        this.participationStatus = participationStatus;
    }

    public Set<UUID> getAnsweredQuestionIds() {
        return answeredQuestionIds;
    }

    public void setAnsweredQuestionIds(Set<UUID> answeredQuestionIds) {
        this.answeredQuestionIds = answeredQuestionIds;
    }

    public Set<AnswerEntity> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerEntity> answers) {
        System.out.println("*****ANSWER'ın SET METHODU ****");
        System.out.println("*****ANSWER'ın SET METHODU ****");
        System.err.println("This answers list null mı " + (this.answers == null));
        if (this.answers == null)
            this.answers = new HashSet<>();

        System.err.println("This answers list null mı " + (answers == null));

        this.answers.clear();
        this.answers.addAll(answers);


        answers.forEach(answerEntity -> answerEntity.setParticipation(this));

        System.out.println("*****ANSWER'ın SET METHODU ****");
        System.out.println("*****ANSWER'ın SET METHODU ****");
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public void setAnswers(Set<AnswerEntity> answers) {
        this.answers = answers;
    }
}
