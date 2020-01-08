package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import solvve.course.domain.MovieReview;

import java.util.UUID;

public interface MovieReviewRepository extends CrudRepository<MovieReview, UUID> {
}
