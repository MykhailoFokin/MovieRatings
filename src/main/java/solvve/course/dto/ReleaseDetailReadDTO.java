package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Country;
import solvve.course.domain.Movie;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ReleaseDetailReadDTO {

    private UUID id;

    private Movie movieId;

    private LocalDate releaseDate;

    private Country countryId;
}
