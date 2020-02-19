package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import solvve.course.domain.MovieSpoilerData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieSpoilerDataRepository extends CrudRepository<MovieSpoilerData, UUID> {

    Optional<List<MovieSpoilerData>> findByMovieReviewIdOrderByIdAsc(UUID movieReviewId);

    Optional<MovieSpoilerData> findByMovieReviewIdAndId(UUID movieReviewId, UUID id);
}
