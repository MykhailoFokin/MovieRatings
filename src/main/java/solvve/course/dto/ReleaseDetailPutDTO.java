package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Country;
import solvve.course.domain.Movie;

import java.time.LocalDate;

@Data
public class ReleaseDetailPutDTO {

    private Movie movieId;

    private LocalDate releaseDate;

    private Country countryId;
}
