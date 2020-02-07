package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieGenreType;

import java.util.List;
import java.util.UUID;

@Data
public class GenreFilter {

    private UUID movieId;

    private List<MovieGenreType> genres;
}
