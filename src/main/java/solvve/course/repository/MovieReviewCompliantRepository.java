package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.MovieReviewCompliant;
import java.util.UUID;

@Repository
public interface MovieReviewCompliantRepository extends CrudRepository<MovieReviewCompliant, UUID> {
}
