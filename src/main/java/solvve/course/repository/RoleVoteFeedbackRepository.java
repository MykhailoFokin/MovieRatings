package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.RoleVoteFeedback;
import java.util.UUID;

@Repository
public interface RoleVoteFeedbackRepository extends CrudRepository<RoleVoteFeedback, UUID> {
}
