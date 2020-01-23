package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Movie;
import solvve.course.domain.ReleaseDetail;

import java.util.Set;
import java.util.UUID;

@Data
public class CountryReadDTO {

    private UUID id;

    private String name;

    private Set<Movie> movies;

    private ReleaseDetail releaseDetail;
}
