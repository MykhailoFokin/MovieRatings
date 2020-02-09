package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.MovieReviewCompliant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieReviewCompliantRepository extends CrudRepository<MovieReviewCompliant, UUID> {

    Optional<MovieReviewCompliant> findByMovieReviewIdAndId(UUID movieReviewId, UUID id);

    Optional<List<MovieReviewCompliant>> findByMovieReviewIdOrderById(UUID movieReviewId);
}
