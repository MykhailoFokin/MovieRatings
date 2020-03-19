package solvve.course.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import solvve.course.domain.ModeratorTypoReviewStatusType;
import solvve.course.domain.UserTypoRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserTypoRequestRepository extends CrudRepository<UserTypoRequest, UUID> {

    @Query("select r from UserTypoRequest r where (r.moderator.id = :moderatorId or r.moderator.id is null)"
            + " and moderatorTypoReviewStatusType in (:moderatorTypoReviewStatusTypes)"
            + " order by r.createdAt asc")
    List<UserTypoRequest> findUserTypoRequestsByModeratorOrRequiredAttention(UUID moderatorId,
                                                                               List<ModeratorTypoReviewStatusType>
                                                                                       moderatorTypoReviewStatusTypes);

    @Query("select r.id from UserTypoRequest r where r.news.id = :newsId and r.sourceText = :sourceText"
            + " and moderatorTypoReviewStatusType in (:moderatorTypoReviewStatusTypes)"
            + " order by r.createdAt asc")
    List<UUID> findUserTypoRequestsOnNewsRequiredAttentionBySourceText(UUID newsId, String sourceText,
                                                                            List<ModeratorTypoReviewStatusType>
                                                                                    moderatorTypoReviewStatusTypes);

    @Query("select r.id from UserTypoRequest r where r.movie.id = movieId and r.sourceText = :sourceText"
            + " and moderatorTypoReviewStatusType in (:moderatorTypoReviewStatusTypes)"
            + " order by r.createdAt asc")
    List<UUID> findUserTypoRequestsOnMovieRequiredAttentionBySourceText(UUID movieId, String sourceText,
                                                                       List<ModeratorTypoReviewStatusType>
                                                                               moderatorTypoReviewStatusTypes);

    @Query("select r.id from UserTypoRequest r where r.role.id = :roleId and r.sourceText = :sourceText"
            + " and moderatorTypoReviewStatusType in (:moderatorTypoReviewStatusTypes)"
            + " order by r.createdAt asc")
    List<UUID> findUserTypoRequestsOnRoleRequiredAttentionBySourceText(UUID roleId, String sourceText,
                                                                       List<ModeratorTypoReviewStatusType>
                                                                               moderatorTypoReviewStatusTypes);

    @Modifying(clearAutomatically = true)
    @Query("update UserTypoRequest r"
            + " set r.moderator.id = :moderatorId"
            + " , r.updatedAt = :updatedAt"
            + " , r.moderatorTypoReviewStatusType = :moderatorTypoReviewStatusType"
            + " where r.id in (:userTypoRequestsId)")
    void updateUserTypoRequestOfSameContentAsObsolete(Instant updatedAt,
                                                ModeratorTypoReviewStatusType moderatorTypoReviewStatusType,
                                              UUID moderatorId, List<UUID> userTypoRequestsId);
}
