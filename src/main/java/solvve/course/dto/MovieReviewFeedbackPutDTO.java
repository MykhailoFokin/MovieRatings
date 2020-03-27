package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class MovieReviewFeedbackPutDTO {

    @NotNull
    private UUID portalUserId;

    @NotNull
    private UUID movieId;

    @NotNull
    private UUID movieReviewId;

    private Boolean isLiked;
}
