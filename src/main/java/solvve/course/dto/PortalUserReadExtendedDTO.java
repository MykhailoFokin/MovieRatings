package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class PortalUserReadExtendedDTO {

    private UUID id;

    private String login;

    private String surname;

    private String name;

    private String middleName;

    private UserType userType;

    private UserConfidenceType userConfidence;

    private List<UserGrant> userGrants;

    private List<MovieReview> movieReview;

    private List<MovieReview> movieReviewModerator;

    private  List<MovieReviewCompliant> movieReviewCompliants;

    private  List<MovieReviewCompliant> movieReviewCompliantsModerator;

    private  List<MovieReviewFeedback> movieReviewFeedbacks;

    private List<RoleReview> roleReviews;

    private List<RoleReview> roleReviewsModerator;

    private  List<RoleReviewCompliant> roleReviewCompliants;

    private  List<RoleReviewCompliant> roleReviewCompliantsModerator;

    private  List<RoleReviewFeedback> roleReviewFeedbacks;

    private List<MovieVote> movieVotes;

    private List<News> news;

    private List<RoleVote> roleVotes;

    private List<Visit> visits;

    private Instant createdAt;

    private Instant updatedAt;
}
