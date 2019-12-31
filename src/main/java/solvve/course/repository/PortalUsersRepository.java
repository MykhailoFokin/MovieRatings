package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.PortalUsers;
import java.util.UUID;

@Repository
public interface PortalUsersRepository extends CrudRepository<PortalUsers, UUID> {
}
