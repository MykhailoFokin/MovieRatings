package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class CrewType extends AbstractEntity {

    private String name;

    @OneToOne(mappedBy = "crewType")
    private Crew crew;
}
