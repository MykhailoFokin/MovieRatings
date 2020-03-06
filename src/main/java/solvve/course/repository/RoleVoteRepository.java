package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.RoleVote;
import java.util.UUID;

@Repository
public interface RoleVoteRepository extends CrudRepository<RoleVote, UUID> {

    @Query("select avg(v.rating) from RoleVote v where v.role.id = :roleId")
    Double calcAverageMarkOfRole(UUID roleId);
}
