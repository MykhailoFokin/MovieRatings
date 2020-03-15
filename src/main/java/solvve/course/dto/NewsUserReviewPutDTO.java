package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.NewsUserReviewStatusType;

import java.util.UUID;

@Data
public class NewsUserReviewPutDTO {

    private UUID portalUserId;

    private UUID newsId;

    private NewsUserReviewStatusType newsUserReviewStatusType;

    private UUID moderatorId;
}
