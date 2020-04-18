package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
public class Person extends AbstractEntity {

    @Size(min = 1, max = 255)
    private String surname;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 255)
    private String middleName;

    @OneToMany(mappedBy = "person", cascade = CascadeType.PERSIST)
    private Set<Crew> crews;

    @OneToOne(mappedBy = "person")
    private Role role;

    private LocalDate birthday;

    private String knownForDepartment;

    private LocalDate deathday;

    private Short gender;

    private String biography;

    private String placeOfBirth;

    private Boolean adult;

    private String imdbId;

    private String homepage;
}
