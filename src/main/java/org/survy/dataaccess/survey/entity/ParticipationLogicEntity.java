package org.survy.dataaccess.survey.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "participation_logics")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public abstract class ParticipationLogicEntity {


    @Id
    private UUID id;

    private String type;

    {
        type = getType();
    }


    @ManyToOne
    @JoinColumn(name = "surveys_id")
    private SurveyEntity survey;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipationLogicEntity that = (ParticipationLogicEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setSurvey(SurveyEntity survey) {
        this.survey = survey;
    }

    public abstract String getType();

    public  void update(ParticipationLogicEntity newLogic) {
        this.id = newLogic.id;
        this.type = newLogic.type;
    }
}
