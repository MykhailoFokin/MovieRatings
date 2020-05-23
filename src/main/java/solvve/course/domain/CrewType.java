package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Entity
public class CrewType extends AbstractEntity {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @OneToMany(mappedBy = "crewType", cascade = CascadeType.PERSIST)
    private Set<Crew> crew;
}
