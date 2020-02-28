package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import solvve.course.domain.RoleReview;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleReviewRepository extends CrudRepository<RoleReview, UUID> {

    Optional<RoleReview> findByPortalUserIdAndId(UUID portalUserId, UUID id);

    Optional<List<RoleReview>> findByPortalUserIdOrderById(UUID portalUserId);
}
