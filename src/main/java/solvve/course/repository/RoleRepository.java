package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Role;
import solvve.course.dto.RoleInLeaderBoardReadDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface RoleRepository extends CrudRepository<Role, UUID> {

    @Query("select r.id from Role r")
    Stream<UUID> getIdsOfRoles();

    @Query("select new solvve.course.dto.RoleInLeaderBoardReadDTO(r.id, r.title, m.title, r.averageRating,"
            + " (select count(v) from RoleVote v where v.role.id = r.id and v.rating > 1))"
            + " from Role r "
            + " join Movie m on r.movie.id = m.id"
            + " order by r.averageRating desc")
    List<RoleInLeaderBoardReadDTO> getRolesLeaderBoard();
}
