package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.NewsUserReview;

import java.util.UUID;

@Repository
public interface NewsUserReviewRepository extends CrudRepository<NewsUserReview, UUID> {
}
