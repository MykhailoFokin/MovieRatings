package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
public class Crew extends AbstractEntity {

    @ManyToOne
    private Person person;

    @ManyToOne
    @NotNull
    private Movie movie;

    @ManyToOne
    @NotNull
    private CrewType crewType;

    @Size(min = 1, max = 1000)
    private String description;
}
