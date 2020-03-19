package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.ModeratorTypoReviewStatusType;

import java.util.UUID;

@Data
public class NewsUserReviewNotePatchDTO {

    private UUID moderatorId;

    private String proposedText;

    private String approvedText;

    private String sourceText;

    private Integer startIndex;

    private Integer endIndex;

    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    private UUID newsUserReviewId;

    private UUID newsId;
}
