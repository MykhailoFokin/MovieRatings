package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PersonReadDTO {

    private UUID id;

    private String surname;

    private String name;

    private String middleName;

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
