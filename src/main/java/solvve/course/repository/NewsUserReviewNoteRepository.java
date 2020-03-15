package solvve.course.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import solvve.course.domain.NewsUserReviewNote;
import solvve.course.domain.NewsUserReviewStatusType;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NewsUserReviewNoteRepository extends CrudRepository<NewsUserReviewNote, UUID> {

    Optional<NewsUserReviewNote> findByNewsUserReviewIdAndIdAndNewsId(UUID newsId, UUID newsUserReviewId, UUID id);

    Optional<List<NewsUserReviewNote>> findByNewsUserReviewIdAndNewsIdOrderById(UUID newsId, UUID newsUserReviewId);

    Optional<List<NewsUserReviewNote>> findByNewsIdAndStartIndexAndEndIndex(UUID newsId, Integer startIndex,
                                                                            Integer endIndex);

    @Query("select n from NewsUserReviewNote n where (n.moderator.id = :userId or n.moderator.id is null)"
            + " and newsUserReviewStatusType in (:newsUserReviewStatusTypes)"
            + " order by n.createdAt asc")
    List<NewsUserReviewNote> findUserReviewNotesByModeratorOrRequiredAttention(UUID userId,
                                                                               List<NewsUserReviewStatusType>
                                                                                       newsUserReviewStatusTypes);

    @Modifying(clearAutomatically = true)
    @Query("update NewsUserReviewNote e"
            + " set e.moderator.id = :moderatorId"
            + " , e.approvedText = :moderatorApprovedText"
            + " , e.updatedAt = :updatedAt"
            + " , e.newsUserReviewStatusType = :newsUserReviewStatusType"
            + " where e.news.id = :newsId and e.newsUserReviewStatusType != :newsUserReviewStatusType"
            + " and e.id in (:newsUserReviewNotes)")
    void updateNewsUserReviewNoteWithSameNote(UUID newsId, NewsUserReviewStatusType newsUserReviewStatusType,
                                              UUID moderatorId, String moderatorApprovedText, Instant updatedAt,
                                              List<UUID> newsUserReviewNotes);
}
