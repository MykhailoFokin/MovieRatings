package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import java.time.Instant;
import java.util.UUID;

@Data
public class MovieReviewCompliantReadDTO {

    private UUID id;

    private UUID userId;

    private UUID movieId;

    private UUID movieReviewId;

    private String description;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;

    private Instant createdAt;

    private Instant modifiedAt;
}
