package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.ModeratorTypoReviewStatusType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class NewsUserReviewPutDTO {

    @NotNull
    private UUID portalUserId;

    @NotNull
    private UUID newsId;

    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    private UUID moderatorId;
}
