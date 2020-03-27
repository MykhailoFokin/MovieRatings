package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.ModeratorTypoReviewStatusType;

import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

@Data
public class UserTypoRequestPatchDTO {

    private UUID moderatorId;

    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    private Instant fixAppliedDate;

    @Size(min = 1, max = 1000)
    private String approvedText;
}
