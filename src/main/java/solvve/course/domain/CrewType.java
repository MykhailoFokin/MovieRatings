package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
public class CrewType extends AbstractEntity {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @OneToOne(mappedBy = "crewType")
    private Crew crew;
}
