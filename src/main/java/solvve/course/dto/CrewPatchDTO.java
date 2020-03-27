package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class CrewPatchDTO {

    private UUID personId;

    private UUID movieId;

    private UUID crewTypeId;

    @Size(min = 1, max = 1000)
    private String description;
}
