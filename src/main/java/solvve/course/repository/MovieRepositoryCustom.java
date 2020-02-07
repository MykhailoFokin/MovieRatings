package solvve.course.repository;

import solvve.course.domain.Movie;
import solvve.course.dto.MovieFilter;

import java.util.List;

public interface MovieRepositoryCustom {

    List<Movie> findByFilter(MovieFilter filter);
}
