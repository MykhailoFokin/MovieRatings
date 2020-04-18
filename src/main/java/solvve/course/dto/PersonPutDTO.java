package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class PersonPutDTO {

    @Size(min = 1, max = 255)
    private String surname;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 255)
    private String middleName;

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
