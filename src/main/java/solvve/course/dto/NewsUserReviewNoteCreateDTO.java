package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.NewsUserReviewStatusType;

import java.util.UUID;

@Data
public class NewsUserReviewNoteCreateDTO {

    private UUID moderatorId;

    private String proposedText;

    private String approvedText;

    private String sourceText;

    private Integer startIndex;

    private Integer endIndex;

    private NewsUserReviewStatusType newsUserReviewStatusType;

    private UUID newsUserReviewId;

    private UUID newsId;
}
