package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.ModeratorTypoReviewStatusType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class NewsUserReviewNotePutDTO {

    private UUID moderatorId;

    @NotNull
    @Size(min = 1, max = 1000)
    private String proposedText;

    @Size(min = 1, max = 1000)
    private String approvedText;

    @NotNull
    @Size(min = 1, max = 1000)
    private String sourceText;

    private Integer startIndex;

    private Integer endIndex;

    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    @NotNull
    private UUID newsUserReviewId;

    @NotNull
    private UUID newsId;
}
