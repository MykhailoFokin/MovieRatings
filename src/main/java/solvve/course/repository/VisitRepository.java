package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Visit;
import solvve.course.domain.VisitStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface VisitRepository extends CrudRepository<Visit, UUID>, VisitRepositoryCustom {

    List<Visit> findByPortalUserIdAndStatusOrderByStartAtAsc(UUID portalUserId, VisitStatus visitStatus);

    @Query("select v from Visit v where v.portalUser.id = :userId and v.status = :visitStatus"
            + " and v.startAt >= :startFrom and v.startAt < :startTo order by v.startAt asc")
    List<Visit> findVisitsForUserInGivenInterval(UUID userId,
                                                   VisitStatus visitStatus,
                                                   Instant startFrom,
                                                   Instant startTo);
}
