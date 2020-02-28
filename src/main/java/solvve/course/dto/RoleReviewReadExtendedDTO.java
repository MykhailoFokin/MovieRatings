package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class RoleReviewReadExtendedDTO {

    private UUID id;

    private PortalUser portalUser;

    private Role role;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private PortalUser moderator;

    private Set<RoleReviewCompliant> roleReviewCompliants;

    private Set<RoleReviewFeedback> roleReviewFeedbacks;

    private Set<RoleSpoilerData> roleSpoilerData;

    private Instant createdAt;

    private Instant updatedAt;
}
