package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Role;

import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface RoleRepository extends CrudRepository<Role, UUID> {

    @Query("select r.id from Role r")
    Stream<UUID> getIdsOfRoles();

}
