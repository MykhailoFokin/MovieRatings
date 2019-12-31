package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.MovieVoteCompliant;
import java.util.UUID;

@Repository
public interface MovieVoteCompliantRepository extends CrudRepository<MovieVoteCompliant, UUID> {
}
