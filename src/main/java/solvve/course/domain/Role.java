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
public class Role {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String description;

    @OneToOne
    @JoinColumn
    private Movie movie;

    @OneToOne
    @JoinColumn
    private Person person;

    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
    private Set<RoleReview> roleReviews;

    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
    private Set<RoleReviewCompliant> roleReviewCompliants;

    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
    private Set<RoleReviewFeedback> roleReviewFeedbacks;

    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
    private Set<RoleVote> roleVotes;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private Double averageRating;

    @OneToMany(mappedBy = "news", cascade = CascadeType.PERSIST)
    private Set<UserTypoRequest> userTypoRequests;
}
