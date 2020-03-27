package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CountryPatchDTO {

    @Size(min = 1, max = 255)
    private String name;
}
