package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.MovieReviewFeedback;
import java.util.UUID;

@Repository
public interface MovieReviewFeedbackRepository extends CrudRepository<MovieReviewFeedback, UUID> {
}
