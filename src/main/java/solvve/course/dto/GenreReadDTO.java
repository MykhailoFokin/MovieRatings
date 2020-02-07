package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieGenreType;

import java.util.UUID;

@Data
public class GenreReadDTO {

    private UUID id;

    private UUID movieId;

    private MovieGenreType name;
}
