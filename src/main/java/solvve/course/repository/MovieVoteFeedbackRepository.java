package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.MovieVoteFeedback;
import java.util.UUID;

@Repository
public interface MovieVoteFeedbackRepository extends CrudRepository<MovieVoteFeedback, UUID> {
}
