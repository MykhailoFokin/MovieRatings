package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class PersonPatchDTO {

    @Size(min = 1, max = 255)
    private String surname;

    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 255)
    private String middleName;
}
