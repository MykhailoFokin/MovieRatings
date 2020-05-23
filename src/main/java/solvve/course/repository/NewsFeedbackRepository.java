package solvve.course.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.NewsFeedback;

import java.util.UUID;

@Repository
public interface NewsFeedbackRepository extends CrudRepository<NewsFeedback, UUID> {

    Integer countByNewsIdAndIsLikedTrue(UUID newsId);

    Integer countByNewsId(UUID newsId);
}
