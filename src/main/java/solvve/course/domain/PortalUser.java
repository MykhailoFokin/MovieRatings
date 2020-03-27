package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class PortalUser extends AbstractEntity {

    @NotNull
    @Size(min = 1, max = 255)
    private String login;

    @Size(min = 1, max = 255)
    private String surname;

    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 255)
    private String middleName;

    @OneToOne
    @JoinColumn(nullable = false)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private UserConfidenceType userConfidence;  // user rating according to activity (set by moderator)

    @OneToMany(mappedBy = "grantedBy", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private  Set<UserGrant> userGrants = new HashSet<UserGrant>();

    @OneToMany(mappedBy = "portalUser", cascade = CascadeType.PERSIST)
    private Set<MovieReview> movieReview = new HashSet<MovieReview>();

    @OneToMany(mappedBy = "moderator", cascade = CascadeType.PERSIST)
    private Set<MovieReview> movieReviewModerator = new HashSet<MovieReview>();

    @OneToMany(mappedBy = "portalUser", cascade = CascadeType.PERSIST)
    private  Set<MovieReviewCompliant> movieReviewCompliants = new HashSet<MovieReviewCompliant>();

    @OneToMany(mappedBy = "moderator", cascade = CascadeType.PERSIST)
    private  Set<MovieReviewCompliant> movieReviewCompliantsModerator = new HashSet<MovieReviewCompliant>();

    @OneToMany(mappedBy = "portalUser", cascade = CascadeType.PERSIST)
    private  Set<MovieReviewFeedback> movieReviewFeedbacks = new HashSet<MovieReviewFeedback>();

    @OneToMany(mappedBy = "portalUser", cascade = CascadeType.PERSIST)
    private Set<RoleReview> roleReviews = new HashSet<RoleReview>();

    @OneToMany(mappedBy = "moderator", cascade = CascadeType.PERSIST)
    private Set<RoleReview> roleReviewsModerator = new HashSet<RoleReview>();

    @OneToMany(mappedBy = "portalUser", cascade = CascadeType.PERSIST)
    private  Set<RoleReviewCompliant> roleReviewCompliants = new HashSet<RoleReviewCompliant>();

    @OneToMany(mappedBy = "moderator", cascade = CascadeType.PERSIST)
    private  Set<RoleReviewCompliant> roleReviewCompliantsModerator = new HashSet<RoleReviewCompliant>();

    @OneToMany(mappedBy = "portalUser", cascade = CascadeType.PERSIST)
    private  Set<RoleReviewFeedback> roleReviewFeedbacks = new HashSet<RoleReviewFeedback>();

    @OneToMany(mappedBy = "portalUser", cascade = CascadeType.PERSIST)
    private Set<MovieVote> movieVotes = new HashSet<MovieVote>();

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.PERSIST)
    private Set<News> news = new HashSet<News>();

    @OneToMany(mappedBy = "portalUser", cascade = CascadeType.PERSIST)
    private Set<RoleVote> roleVotes = new HashSet<RoleVote>();

    @OneToMany(mappedBy = "portalUser", cascade = CascadeType.PERSIST)
    private Set<Visit> visits = new HashSet<Visit>();

    @OneToMany(mappedBy = "portalUser", cascade = CascadeType.PERSIST)
    private Set<NewsUserReview> newsUserReviews = new HashSet<NewsUserReview>();

    @OneToMany(mappedBy = "moderator", cascade = CascadeType.PERSIST)
    private Set<NewsUserReview> newsUserReviewsModerator = new HashSet<NewsUserReview>();

    @OneToMany(mappedBy = "moderator", cascade = CascadeType.PERSIST)
    private Set<NewsUserReviewNote> newsUserReviewNotes = new HashSet<NewsUserReviewNote>();
}
