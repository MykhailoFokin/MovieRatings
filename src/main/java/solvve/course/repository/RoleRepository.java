package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Role;
import java.util.UUID;

@Repository
public interface RoleRepository extends CrudRepository<Role, UUID> {

}
