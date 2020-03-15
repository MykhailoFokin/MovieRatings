package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.NewsUserReviewStatusType;

import java.time.Instant;
import java.util.UUID;

@Data
public class NewsUserReviewReadDTO {

    private UUID id;

    private UUID portalUserId;

    private UUID newsId;

    private NewsUserReviewStatusType newsUserReviewStatusType;

    private UUID moderatorId;

    private Instant createdAt;

    private Instant updatedAt;
}
