package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import java.util.UUID;

@Data
public class RoleReviewCompliantCreateDTO {

    private UUID portalUserId;

    private UUID roleId;

    private UUID roleReviewId;

    private String description;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;
}
