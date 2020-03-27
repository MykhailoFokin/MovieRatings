package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class MovieReviewFeedbackCreateDTO {

    @NotNull
    private UUID portalUserId;

    @NotNull
    private UUID movieId;

    @NotNull
    private UUID movieReviewId;

    private Boolean isLiked;
}
