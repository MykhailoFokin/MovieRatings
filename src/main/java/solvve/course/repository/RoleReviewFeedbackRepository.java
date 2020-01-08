package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.RoleReviewFeedback;
import java.util.UUID;

@Repository
public interface RoleReviewFeedbackRepository extends CrudRepository<RoleReviewFeedback, UUID> {
}
