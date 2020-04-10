package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieProductionType;

import java.util.UUID;

@Data
public class MovieMovieCompanyReadDTO {

    private UUID id;

    private MovieProductionType movieProductionType;

    private String description;
}
