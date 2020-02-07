package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Genre;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieGenreType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface GenreRepository extends CrudRepository<Genre, UUID>, GenreRepositoryCustom {

    List<Genre> findByMovieIdOrderByNameAsc(Movie movieId);

    @Query("select g from Genre g where g.movieId.id = :movieId and g.name = :movieGenreType")
    List<Genre> findGenreForMovieAndGenreName(UUID movieId, MovieGenreType movieGenreType);
}
