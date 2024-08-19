package org.survy.dataaccess.question.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.survy.dataaccess.survey.entity.SurveyEntity;

import java.util.Objects;
import java.util.UUID;



@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator-type")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class QuestionEntity {
    @Id
    private UUID id ;

    @Column(name = "question_text")
    private String questionText ;

    private String type;
    @Column(name = "page" , nullable = false , columnDefinition = "int default 0")
    private Integer page ;
    @Column(name = "position" , nullable = false , columnDefinition = "int default 0")
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveys_id")
    private SurveyEntity survey;


    {
        type = getType();
    }

    public QuestionEntity() {

    }

    public QuestionEntity(UUID id, String questionText, int page, int order, SurveyEntity survey , String type) {
        this.id = id;
        this.questionText = questionText;
        this.page = page;
        this.position = order;
        this.survey = survey;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionEntity that = (QuestionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SurveyEntity getSurvey() {
        return survey;
    }

    public void setSurvey(SurveyEntity survey) {
        this.survey = survey;
    }

    public abstract String getType();

    public void setType(String type) {
        this.type = type;
    }


    public  void  update(QuestionEntity newQuestion){
        this.setId(newQuestion.getId());
        this.setQuestionText(newQuestion.getQuestionText());
        this.setPosition(newQuestion.getPosition());
        this.setPage(newQuestion.getPage());
        this.setType(newQuestion.getType());

    }

}
