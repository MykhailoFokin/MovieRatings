package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class MovieReviewFeedbackReadDTO {

    private UUID id;

    private UUID portalUserId;

    private UUID movieId;

    private UUID movieReviewId;

    private Boolean isLiked;

    private Instant createdAt;

    private Instant updatedAt;
}
