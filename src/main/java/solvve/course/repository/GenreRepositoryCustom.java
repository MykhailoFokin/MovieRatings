package solvve.course.repository;

import solvve.course.domain.Genre;
import solvve.course.dto.GenreFilter;

import java.util.List;

public interface GenreRepositoryCustom {

    List<Genre> findByFilter(GenreFilter genreFilter);
}
