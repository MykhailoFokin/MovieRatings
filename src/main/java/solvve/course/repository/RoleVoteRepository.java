package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.RoleVote;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleVoteRepository extends CrudRepository<RoleVote, UUID> {

    @Query("select avg(v.rating) from RoleVote v where v.role.id = :roleId")
    Double calcAverageMarkOfRole(UUID roleId);

    List<RoleVote> findByRoleIdAndPortalUserId(UUID roleId, UUID portalUserId);

    @Query("select avg(v.rating) from RoleVote v"
            + " join Role r on r.id = v.role.id"
            + " join Person p on p.id = r.person.id"
            + " where p.id = :personId")
    Double calcAverageMarkOfRoleForPerson(UUID personId);
}
