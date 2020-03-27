package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class MovieReviewCompliantCreateDTO {

    @NotNull
    private UUID portalUserId;

    @NotNull
    private UUID movieId;

    @NotNull
    private UUID movieReviewId;

    @Size(min = 1, max = 1000)
    private String description;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;
}
