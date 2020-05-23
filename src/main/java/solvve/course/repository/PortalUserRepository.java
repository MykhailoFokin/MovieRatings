package solvve.course.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserConfidenceType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface PortalUserRepository extends CrudRepository<PortalUser, UUID> {

    PortalUser findByEmail(String email);

    boolean existsByIdAndEmail(UUID id, String email);

    List<PortalUser> findByUserConfidence(UserConfidenceType userConfidence);

    @Modifying(clearAutomatically = true)
    @Query("update PortalUser r"
            + " set r.userConfidence = :userConfidenceType"
            + " , r.updatedAt = :updatedAt"
            + " where r.id = :portalUserId")
    void updateUserConfidence(UUID portalUserId, Instant updatedAt, UserConfidenceType userConfidenceType);

    boolean existsByIdAndUserConfidence(UUID id, UserConfidenceType userConfidence);
}
