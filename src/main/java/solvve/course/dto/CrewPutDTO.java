package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CrewPutDTO {

    private UUID personId;

    private UUID movieId;

    private UUID crewTypeId;

    private String description;
}
