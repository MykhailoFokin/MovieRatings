package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.PortalUser;
import solvve.course.domain.Role;
import solvve.course.domain.RoleReview;
import solvve.course.domain.UserModeratedStatusType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Data
public class RoleReviewCompliantPatchDTO {

    private PortalUser userId;

    private Role roleId;

    private RoleReview roleReviewId;

    private String description;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

    private PortalUser moderatorId;
}
