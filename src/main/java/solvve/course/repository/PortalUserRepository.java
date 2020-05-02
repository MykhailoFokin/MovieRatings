package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.PortalUser;
import java.util.UUID;

@Repository
public interface PortalUserRepository extends CrudRepository<PortalUser, UUID> {

    PortalUser findByEmail(String email);

    boolean existsByIdAndEmail(UUID id, String email);
}
