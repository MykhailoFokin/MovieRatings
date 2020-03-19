package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.ModeratorTypoReviewStatusType;

import java.time.Instant;
import java.util.UUID;

@Data
public class UserTypoRequestPutDTO {

    private UUID moderatorId;

    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    private Instant fixAppliedDate;

    private String approvedText;
}
