package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import java.util.UUID;

@Data
public class RoleReviewReadDTO {

    private UUID id;

    private UUID userId;

    private UUID roleId;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;
}
