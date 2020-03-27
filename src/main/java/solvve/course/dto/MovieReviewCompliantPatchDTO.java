package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class MovieReviewCompliantPatchDTO {

    private UUID portalUserId;

    private UUID movieId;

    private UUID movieReviewId;

    @Size(min = 1, max = 1000)
    private String description;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;
}
