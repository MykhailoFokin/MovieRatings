package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import solvve.course.domain.RoleSpoilerData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleSpoilerDataRepository extends CrudRepository<RoleSpoilerData, UUID> {

    Optional<List<RoleSpoilerData>> findByRoleReviewIdOrderByIdAsc(UUID roleReviewId);

    Optional<RoleSpoilerData> findByRoleReviewIdAndId(UUID roleReviewId, UUID id);
}
