package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import java.util.UUID;

@Data
public class RoleReviewPutDTO {

    private UUID portalUserId;

    private UUID roleId;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;
}
