package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import solvve.course.domain.Movie;
import java.util.UUID;

@Repository
public interface MovieRepository extends CrudRepository<Movie, UUID> {

}