package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CrewTypePutDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;
}
