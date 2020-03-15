package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.NewsUserReviewStatusType;

import java.util.UUID;

@Data
public class NewsUserReviewCreateDTO {

    private UUID portalUserId;

    private UUID newsId;

    private NewsUserReviewStatusType newsUserReviewStatusType;

    private UUID moderatorId;
}
