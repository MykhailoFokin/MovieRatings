package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CrewPatchDTO {

    private UUID personId;

    private UUID movieId;

    private UUID crewTypeId;

    private String description;
}
