package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Entity
public class Role extends AbstractEntity {

    private String title;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType roleType;

    @Size(min = 1, max = 1000)
    private String description;

    @ManyToOne
    @JoinColumn
    @NotNull
    private Movie movie;

    @ManyToOne
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
