package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.PortalUser;
import solvve.course.domain.Role;
import solvve.course.domain.RoleReview;

import java.util.UUID;

@Data
public class RoleReviewFeedbackReadDTO {

    private UUID id;

    private PortalUser userId;

    private Role roleId;

    private RoleReview roleReviewId;

    private Boolean isLiked;
}
