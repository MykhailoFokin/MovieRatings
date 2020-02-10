package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Role {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String roleType; // main, second, etc

    private String description;

    @OneToOne
    @JoinColumn
    private Person personId;

    @OneToMany(mappedBy = "roleId", cascade = CascadeType.REMOVE)
    private Set<RoleReview> roleReviewSet;

    @OneToMany(mappedBy = "roleId", cascade = CascadeType.REMOVE)
    private Set<RoleReviewCompliant> roleReviewCompliants;

    @OneToMany(mappedBy = "roleId", cascade = CascadeType.REMOVE)
    private Set<RoleReviewFeedback> roleReviewFeedbacks;

    @OneToMany(mappedBy = "roleId", cascade = CascadeType.REMOVE)
    private Set<RoleVote> roleVotes;
}
