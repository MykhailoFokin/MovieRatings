package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Country;
import solvve.course.domain.Movie;

import java.time.LocalDate;

@Data
public class ReleaseDetailPatchDTO {

    private Movie movieId;

    private LocalDate releaseDate;

    private Country countryId;
}
