package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.Movie;
import solvve.course.dto.MovieFilter;

public interface MovieRepositoryCustom {

    Page<Movie> findByFilter(MovieFilter filter, Pageable pageable);
}
