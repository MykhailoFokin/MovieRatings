package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Movie;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class CountriesReadDTO {

    private UUID id;

    private String name;

    private Set<Movie> movies = new HashSet<>();
}
