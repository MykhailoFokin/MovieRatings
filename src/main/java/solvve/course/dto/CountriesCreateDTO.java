package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Movie;
import solvve.course.domain.ReleaseDetails;

import java.util.HashSet;
import java.util.Set;

@Data
public class CountriesCreateDTO {

    private String name;

    private Set<Movie> movies;

    private ReleaseDetails releaseDetails;
}
