package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.MovieVote;
import java.util.UUID;

@Repository
public interface MovieVoteRepository extends CrudRepository<MovieVote, UUID> {
}
