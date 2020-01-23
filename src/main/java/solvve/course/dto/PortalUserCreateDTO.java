package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.util.Set;
import java.util.UUID;

@Data
public class PortalUserCreateDTO {

    private String login;

    private String surname;

    private String name;

    private String middleName;

    private UserTypes userType;

    private UserConfidenceType userConfidence;

    private  Set<Grants> grants;

    private Set<MovieReview> movieReview;

    private Set<MovieReview> movieReviewModerator;

    private  Set<MovieReviewCompliant> movieReviewCompliants;

    private  Set<MovieReviewCompliant> movieReviewCompliantsModerator;

    private  Set<MovieReviewFeedback> movieReviewFeedbacks;

    private Set<RoleReview> roleReviews;

    private Set<RoleReview> roleReviewsModerator;

    private  Set<RoleReviewCompliant> roleReviewCompliants;

    private  Set<RoleReviewCompliant> roleReviewCompliantsModerator;

    private  Set<RoleReviewFeedback> roleReviewFeedbacks;

    private Set<MovieVote> movieVotes;

    private Set<News> news;

    private Set<RoleVote> roleVotes;

    private Set<Visit> visits;
}
