package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.CrewType;
import solvve.course.domain.Movie;
import solvve.course.domain.Person;

import java.util.UUID;

@Data
public class CrewReadDTO {

    private UUID id;

    private UUID personId;

    private UUID movieId;

    private UUID crewType;

    private String description;
}
