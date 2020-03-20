package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Person extends AbstractEntity {

    private String surname;

    private String name;

    private String middleName;

    @OneToMany(mappedBy = "person", cascade = CascadeType.PERSIST)
    private Set<Crew> crews;

    @OneToOne(mappedBy = "person")
    private Role role;
}
