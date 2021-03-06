package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.ModeratorTypoReviewStatusType;

import java.time.Instant;
import java.util.UUID;

@Data
public class NewsUserReviewNoteReadDTO {

    private UUID id;

    private UUID moderatorId;

    private String proposedText;

    private String approvedText;

    private String sourceText;

    private Integer startIndex;

    private Integer endIndex;

    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    private UUID newsUserReviewId;

    private Instant createdAt;

    private Instant updatedAt;

    private UUID newsId;
}
