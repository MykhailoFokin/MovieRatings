package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Crew {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Person personId;

    @ManyToOne
    private Movie movieId;

    @OneToOne
    private CrewType crewTypeId = new CrewType();

    private String description;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
