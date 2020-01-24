package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Movie;
import solvve.course.domain.ReleaseDetail;

import java.util.Set;

@Data
public class CountryPutDTO {

    private String name;

    private Set<Movie> movies;

    private ReleaseDetail releaseDetail;
}
