package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class CrewPutDTO {

    private UUID personId;

    @NotNull
    private UUID movieId;

    @NotNull
    private UUID crewTypeId;

    @Size(min = 1, max = 1000)
    private String description;
}
