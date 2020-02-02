package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CrewCreateDTO {

    private UUID personId;

    private UUID movieId;

    private UUID crewType;

    private String description;
}
