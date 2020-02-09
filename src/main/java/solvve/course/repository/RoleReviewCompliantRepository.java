package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.RoleReviewCompliant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleReviewCompliantRepository extends CrudRepository<RoleReviewCompliant, UUID> {

    Optional<RoleReviewCompliant> findByRoleReviewIdAndId(UUID roleReviewId, UUID id);

    Optional<List<RoleReviewCompliant>> findByRoleReviewIdOrderById(UUID roleReviewId);
}
