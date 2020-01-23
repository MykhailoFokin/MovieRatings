package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Countries;
import solvve.course.domain.Movie;

import java.sql.Date;
import java.util.UUID;

@Data
public class ReleaseDetailsCreateDTO {

    private Movie movieId;

    private Date releaseDate;

    private Countries countryId;
}
