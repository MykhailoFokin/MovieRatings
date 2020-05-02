package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Crew;
import solvve.course.domain.Role;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class PersonReadExtendedDTO {

    private UUID id;

    private String surname;

    private String name;

    private String middleName;

    private List<Crew> crews;

    private Role role;

    private Instant createdAt;

    private Instant updatedAt;

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
