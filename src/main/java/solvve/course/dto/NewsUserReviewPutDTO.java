package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.ModeratorTypoReviewStatusType;

import java.util.UUID;

@Data
public class NewsUserReviewPutDTO {

    private UUID portalUserId;

    private UUID newsId;

    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    private UUID moderatorId;
}
