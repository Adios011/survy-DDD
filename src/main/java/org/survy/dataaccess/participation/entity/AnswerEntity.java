package org.survy.dataaccess.participation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.survy.domain.core.entity.Participation;
import org.survy.domain.core.valueObject.QuestionId;

import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "answers")

public class AnswerEntity {

    @Id
    @Column(nullable = false,updatable = false)
    private UUID id ;

    private UUID questionId ;

    private String answerText;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id" )
    private ParticipationEntity participation;

    public AnswerEntity(UUID id) {
        this.id = id;
    }

    public AnswerEntity(UUID id, String answerText, UUID questionId) {
        this.id = id;
        this.questionId = questionId;
        this.answerText = answerText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerEntity that = (AnswerEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
