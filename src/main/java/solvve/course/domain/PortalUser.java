package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class PortalUser {

    @Id
    @GeneratedValue
    private UUID id;

    private String login;

    private String surname;

    private String name;

    private String middleName;

    @OneToOne
    @JoinColumn(nullable = false)
    private UserType userTypeId;

    @Enumerated(EnumType.STRING)
    private UserConfidenceType userConfidence;  // user rating according to activity (set by moderator)

    @OneToMany(mappedBy = "grantedBy")
    private  Set<UserGrant> userGrants;

    @OneToMany(mappedBy = "userId")
    private Set<MovieReview> movieReview;

    @OneToMany(mappedBy = "moderatorId")
    private Set<MovieReview> movieReviewModerator;

    @OneToMany(mappedBy = "userId")
    private  Set<MovieReviewCompliant> movieReviewCompliants;

    @OneToMany(mappedBy = "moderatorId")
    private  Set<MovieReviewCompliant> movieReviewCompliantsModerator;

    @OneToMany(mappedBy = "userId")
    private  Set<MovieReviewFeedback> movieReviewFeedbacks;

    @OneToMany(mappedBy = "userId")
    private Set<RoleReview> roleReviews;

    @OneToMany(mappedBy = "moderatorId")
    private Set<RoleReview> roleReviewsModerator;

    @OneToMany(mappedBy = "userId")
    private  Set<RoleReviewCompliant> roleReviewCompliants;

    @OneToMany(mappedBy = "moderatorId")
    private  Set<RoleReviewCompliant> roleReviewCompliantsModerator;

    @OneToMany(mappedBy = "userId")
    private  Set<RoleReviewFeedback> roleReviewFeedbacks;

    @OneToMany(mappedBy = "userId")
    private Set<MovieVote> movieVotes;

    @OneToMany(mappedBy = "userId")
    private Set<News> news;

    @OneToMany(mappedBy = "userId")
    private Set<RoleVote> roleVotes;

    @OneToMany(mappedBy = "userId")
    private Set<Visit> visits;
}
