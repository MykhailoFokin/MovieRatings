package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class Persons {

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
