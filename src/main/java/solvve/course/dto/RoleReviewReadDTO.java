package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import java.time.Instant;
import java.util.UUID;

@Data
public class RoleReviewReadDTO {

    private UUID id;

    private UUID userId;

    private UUID roleId;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;

    private Instant createdAt;

    private Instant modifiedAt;
}
