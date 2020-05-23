package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import solvve.course.domain.MovieReview;
import solvve.course.domain.UserModeratedStatusType;

import java.util.List;
import java.util.UUID;

public interface MovieReviewRepository extends CrudRepository<MovieReview, UUID> {

    List<MovieReview> findByMovieIdAndModeratedStatus(
            UUID movieId, UserModeratedStatusType moderatedStatus);
}
