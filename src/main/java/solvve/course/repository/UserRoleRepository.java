package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, UUID> {

    UserRole findByUserGroupType(UserGroupType userGroupType);

    @Query("select u from UserRole u")
    List<UserRole> findAllUserRoles();

    @Query("select u from UserRole u join u.users p "
            + " where p.id = :portalUserId ")
    Optional<List<UserRole>> findUserRolesByPortalUserId(UUID portalUserId);
}
