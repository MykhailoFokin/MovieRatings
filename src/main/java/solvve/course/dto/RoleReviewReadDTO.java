package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.util.Set;
import java.util.UUID;

@Data
public class RoleReviewReadDTO {

    private UUID id;

    private PortalUser userId;

    private Role roleId;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private PortalUser moderatorId;

    private Set<RoleReviewCompliant> roleReviewCompliants;

    private Set<RoleReviewFeedback> roleReviewFeedbacks;

    private Set<RoleSpoilerData> roleSpoilerData;
}
