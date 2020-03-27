package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class MovieReviewPatchDTO {

    private UUID portalUserId;

    private UUID movieId;

    @Size(min = 1, max = 1000)
    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;
}
