package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.MovieReviewFeedback;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieReviewFeedbackRepository extends CrudRepository<MovieReviewFeedback, UUID> {

    Optional<MovieReviewFeedback> findByMovieReviewIdAndId(UUID movieReviewId, UUID id);

    Optional<List<MovieReviewFeedback>> findByMovieReviewIdOrderById(UUID movieReviewId);
}
