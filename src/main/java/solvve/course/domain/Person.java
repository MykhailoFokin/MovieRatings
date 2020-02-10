package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Person {

    @Id
    @GeneratedValue
    private UUID id;

    private String surname;

    private String name;

    private String middleName;

    @OneToMany(mappedBy = "personId")
    private Set<Crew> crews;

    @OneToOne(mappedBy = "personId")
    private Role role;
}
