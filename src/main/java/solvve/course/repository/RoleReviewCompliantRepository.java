package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.RoleReviewCompliant;
import java.util.UUID;

@Repository
public interface RoleReviewCompliantRepository extends CrudRepository<RoleReviewCompliant, UUID> {
}
