package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieGenreType;

import java.time.Instant;
import java.util.UUID;

@Data
public class GenreReadDTO {

    private UUID id;

    private UUID movieId;

    private MovieGenreType name;

    private Instant createdAt;

    private Instant modifiedAt;
}
