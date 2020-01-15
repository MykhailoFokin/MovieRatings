package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Movie;

import java.util.HashSet;
import java.util.Set;

@Data
public class CountriesPatchDTO {

    private String name;

    private Set<Movie> movies = new HashSet<>();
}
