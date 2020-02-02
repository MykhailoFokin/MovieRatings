package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MovieReviewFeedbackPatchDTO {

    private UUID userId;

    private UUID movieId;

    private UUID movieReviewId;

    private Boolean isLiked;
}
