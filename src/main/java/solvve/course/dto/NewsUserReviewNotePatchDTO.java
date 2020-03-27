package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.ModeratorTypoReviewStatusType;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class NewsUserReviewNotePatchDTO {

    private UUID moderatorId;

    @Size(min = 1, max = 1000)
    private String proposedText;

    @Size(min = 1, max = 1000)
    private String approvedText;

    @Size(min = 1, max = 1000)
    private String sourceText;

    private Integer startIndex;

    private Integer endIndex;

    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    private UUID newsUserReviewId;

    private UUID newsId;
}
