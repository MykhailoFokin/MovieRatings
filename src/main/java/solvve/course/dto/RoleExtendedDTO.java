package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.util.Set;
import java.util.UUID;

@Data
public class RoleExtendedDTO {

    private UUID id;

    private String title;

    private String roleType;

    private String description;

    private Person personId;

    private Set<RoleReview> roleReviewSet;

    private Set<RoleReviewCompliant> roleReviewCompliants;

    private Set<RoleReviewFeedback> roleReviewFeedbacks;

    private Set<RoleVote> roleVotes;
}
