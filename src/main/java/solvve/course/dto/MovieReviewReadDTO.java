package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import java.time.Instant;
import java.util.UUID;

@Data
public class MovieReviewReadDTO {

    private UUID id;

    private UUID userId;

    private UUID movieId;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;

    private Instant createdAt;

    private Instant modifiedAt;
}
