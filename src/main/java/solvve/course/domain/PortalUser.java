package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @OneToMany(mappedBy = "grantedBy", cascade = CascadeType.PERSIST)
    private  Set<UserGrant> userGrants;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    private Set<MovieReview> movieReview;

    @OneToMany(mappedBy = "moderatorId", cascade = CascadeType.PERSIST)
    private Set<MovieReview> movieReviewModerator;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    private  Set<MovieReviewCompliant> movieReviewCompliants;

    @OneToMany(mappedBy = "moderatorId", cascade = CascadeType.PERSIST)
    private  Set<MovieReviewCompliant> movieReviewCompliantsModerator;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    private  Set<MovieReviewFeedback> movieReviewFeedbacks;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    private Set<RoleReview> roleReviews;

    @OneToMany(mappedBy = "moderatorId", cascade = CascadeType.PERSIST)
    private Set<RoleReview> roleReviewsModerator;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    private  Set<RoleReviewCompliant> roleReviewCompliants;

    @OneToMany(mappedBy = "moderatorId", cascade = CascadeType.PERSIST)
    private  Set<RoleReviewCompliant> roleReviewCompliantsModerator;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    private  Set<RoleReviewFeedback> roleReviewFeedbacks;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    private Set<MovieVote> movieVotes;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    private Set<News> news;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    private Set<RoleVote> roleVotes;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.PERSIST)
    private Set<Visit> visits;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;
}
