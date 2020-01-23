package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.CrewType;
import solvve.course.domain.Movie;
import solvve.course.domain.Persons;

import java.util.UUID;

@Data
public class CrewCreateDTO {

    private Persons personId;

    private Movie movieId;

    private CrewType crewType;

    private String description;
}
