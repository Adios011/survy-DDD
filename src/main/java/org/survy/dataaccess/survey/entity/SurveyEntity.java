package org.survy.dataaccess.survey.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.type.SqlTypes;
import org.survy.dataaccess.participation.entity.ParticipationEntity;
import org.survy.dataaccess.question.entity.QuestionEntity;
import org.survy.domain.core.entity.logic.ParticipationLogic;
import org.survy.domain.core.entity.question.Question;
import org.survy.domain.core.valueObject.ParticipationLogicTypes;
import org.survy.domain.core.valueObject.SurveyStatus;

import java.util.*;

@NamedEntityGraph(name = "survey.questionsAndParticipationLogics" ,
attributeNodes = {
        @NamedAttributeNode(value = "questions"),
        @NamedAttributeNode(value = "participationLogics")
})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "surveys")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SurveyEntity {
    @Id
    private UUID id;

    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<UUID> creators;

    private String title;
    private String description;
    @Temporal(TemporalType.DATE)
    private Date closeDate;
    private String category;

    private String closureReason;

    @Enumerated(EnumType.STRING)
    private SurveyStatus surveyStatus;



    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL , fetch = FetchType.LAZY , orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<QuestionEntity> questions;



    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL , fetch = FetchType.LAZY , orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ParticipationLogicEntity> participationLogics;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyEntity that = (SurveyEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public void setQuestions(Set<QuestionEntity> newQuestions) {
        if(this.questions == null)
            this.questions = new HashSet<>();

        HashMap<UUID , QuestionEntity> currentQuestions = new HashMap<>();
        for (QuestionEntity question : this.questions)
            currentQuestions.put(question.getId() , question);


        for (QuestionEntity newQuestion : newQuestions) {
            UUID newQuestionId = newQuestion.getId();
            QuestionEntity current = currentQuestions.get(newQuestionId);
            if(current == null)
                this.questions.add(newQuestion);
            else
                current.update(newQuestion);

            this.questions.forEach(questionEntity -> questionEntity.setSurvey(this));
        }



        questions.forEach(questionEntity -> questionEntity.setSurvey(this));
    }

    public void setParticipationLogics(List<ParticipationLogicEntity> newLogics ){
       if(this.participationLogics == null)
           this.participationLogics = new HashSet<>();

       HashMap<UUID , ParticipationLogicEntity> currentLogics = new HashMap<>();

        for (ParticipationLogicEntity current : this.participationLogics)
            currentLogics.put(current.getId(), current);



        for (ParticipationLogicEntity newLogic : newLogics) {
            UUID newLogicId = newLogic.getId();
            ParticipationLogicEntity current = currentLogics.get(newLogicId);
            if(current == null)
                this.participationLogics.add(newLogic);
            else
                current.update(newLogic);

            this.participationLogics.forEach(participationLogic -> participationLogic.setSurvey(this));
        }


       newLogics.forEach(participationLogic -> participationLogic.setSurvey(this));
    }

    public Set<ParticipationLogicEntity> getParticipationLogics() {
        return participationLogics;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<UUID> getCreators() {
        return creators;
    }

    public void setCreators(List<UUID> creators) {
        this.creators = creators;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public SurveyStatus getSurveyStatus() {
        return surveyStatus;
    }

    public void setSurveyStatus(SurveyStatus surveyStatus) {
        this.surveyStatus = surveyStatus;
    }

    public Set<QuestionEntity> getQuestions() {
        return questions;
    }

    public String getClosureReason() {
        return closureReason;
    }

    public void setClosureReason(String closureReason) {
        this.closureReason = closureReason;
    }


    @Override
    public String toString() {
        return "SurveyEntity{" +
                "id=" + id +
                ", creators=" + creators +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", closeDate=" + closeDate +
                ", category='" + category + '\'' +
                ", closureReason='" + closureReason + '\'' +
                ", surveyStatus=" + surveyStatus +
                ", questions=" + questions +
                ", participationLogics=" + participationLogics +
                '}';
    }
}
