package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class RoleReviewFeedbackReadDTO {

    private UUID id;

    private UUID userId;

    private UUID roleId;

    private UUID roleReviewId;

    private Boolean isLiked;

    private Instant createdAt;

    private Instant modifiedAt;
}
