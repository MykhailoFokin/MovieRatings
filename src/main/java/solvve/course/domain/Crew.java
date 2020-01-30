package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class Crew {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Person personId;

    @ManyToOne
    private Movie movieId;

    @OneToOne
    private CrewType crewType;

    private String description;
}
