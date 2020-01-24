package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.PortalUser;
import solvve.course.domain.Role;
import solvve.course.domain.RoleReview;
import solvve.course.domain.UserModeratedStatusType;

@Data
public class RoleReviewCompliantPutDTO {

    private PortalUser userId;

    private Role roleId;

    private RoleReview roleReviewId;

    private String description;

    private UserModeratedStatusType moderatedStatus;

    private PortalUser moderatorId;
}
