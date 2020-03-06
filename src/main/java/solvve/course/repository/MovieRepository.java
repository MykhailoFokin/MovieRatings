package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.Movie;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public interface MovieRepository extends CrudRepository<Movie, UUID>, MovieRepositoryCustom {

    @Query("select m from Movie m left join MovieReview mr on mr.movie.id = m.id and mr.portalUser.id = :portalUserId"
            + " and mr.updatedAt >= :startFrom and mr.updatedAt < :startTo"
            + " left join MovieReviewCompliant mrc on mrc.movie.id = m.id and mrc.portalUser.id = :portalUserId and "
            + "mrc.updatedAt >= :startFrom and mrc.updatedAt < :startTo"
            + " left join MovieReviewFeedback mrf on mrf.movie.id = m.id and mrf.portalUser.id = :portalUserId and "
            + "mrf.updatedAt >= :startFrom and mrf.updatedAt < :startTo"
            + " where (mr.id is not null)"
            + " or (mrc.id is not null)"
            + " or (mrf.id is not null) "
            + " order by m.createdAt desc")
    List<Movie> findPortalUserTouchedMoviesInGivenInterval(UUID portalUserId,
                                                 Instant startFrom,
                                                 Instant startTo);

    @Query("select m0 from Movie m0 join MovieReviewFeedback mrf0 on mrf0.movie.id = m0.id"
            + " where exists ("
            + " select 1 from MovieReviewFeedback mrf1 where exists ("
            + " select 2 from Movie m2 join MovieReviewFeedback mrf2 on mrf2.movie.id = m2.id "
            + " where mrf2.portalUser.id = :portalUserId and mrf2.movie.id = mrf1.movie.id)"
            + " and mrf1.portalUser.id != :portalUserId and mrf1.portalUser.id = mrf0.portalUser.id)"
            + " and not exists (select 1 from Movie m3 join MovieReviewFeedback mrf3 on mrf3.movie.id = m3.id "
            + " where mrf3.portalUser.id = :portalUserId and m3.id = mrf0.movie.id)"
            + " order by m0.createdAt desc")
    List<Movie> findPortalUserRecommendedMovies(UUID portalUserId);

    @Query("select m.id from Movie m")
    Stream<UUID> getIdsOfMovies();
}