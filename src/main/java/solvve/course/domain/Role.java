package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Role extends AbstractEntity {

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

    private Double averageRating;

    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
    private Set<UserTypoRequest> userTypoRequests;
}
