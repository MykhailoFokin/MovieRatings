package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.ModeratorTypoReviewStatusType;

import java.time.Instant;
import java.util.UUID;

@Data
public class NewsUserReviewReadDTO {

    private UUID id;

    private UUID portalUserId;

    private UUID newsId;

    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    private UUID moderatorId;

    private Instant createdAt;

    private Instant updatedAt;
}
