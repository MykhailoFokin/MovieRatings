package solvve.course.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.MovieVote;
import java.util.UUID;

@Repository
public interface MovieVoteRepository extends CrudRepository<MovieVote, UUID> {

    @Query("select avg(v.rating) from MovieVote v where v.movie.id = :movieId")
    Double calcAverageMarkOfMovie(UUID movieId);

    @Query("select avg(v.rating) from MovieVote v"
            + " join Movie m on m.id = v.movie.id"
            + " join Role r on r.movie.id = m.id"
            + " join Person p on p.id = r.person.id"
            + " where p.id = :personId")
    Double calcAverageMarkOfMovieForPerson(UUID personId);
}
