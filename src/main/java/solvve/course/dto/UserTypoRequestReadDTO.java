package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.ModeratorTypoReviewStatusType;

import java.time.Instant;
import java.util.UUID;

@Data
public class UserTypoRequestReadDTO {

    private UUID id;

    private UUID requesterId;

    private UUID moderatorId;

    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    private Instant fixAppliedDate;

    private String sourceText;

    private String proposedText;

    private String approvedText;

    private UUID newsId;

    private UUID movieId;

    private UUID roleId;

    private Instant createdAt;

    private Instant updatedAt;
}
