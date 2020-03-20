package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Crew extends AbstractEntity {

    @ManyToOne
    private Person person;

    @ManyToOne
    private Movie movie;

    @OneToOne
    private CrewType crewType;

    private String description;
}
