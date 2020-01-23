package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.CrewType;
import solvve.course.domain.Movie;
import solvve.course.domain.Person;

import java.util.UUID;

@Data
public class CrewReadDTO {

    private UUID id;

    private Person personId;

    private Movie movieId;

    private CrewType crewType;

    private String description;
}
