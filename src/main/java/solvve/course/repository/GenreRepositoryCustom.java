package solvve.course.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solvve.course.domain.Genre;
import solvve.course.dto.GenreFilter;

public interface GenreRepositoryCustom {

    Page<Genre> findByFilter(GenreFilter genreFilter, Pageable pageable);
}
