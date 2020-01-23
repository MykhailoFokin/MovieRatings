package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String roleType; // main, second, etc

    private String description;

    @OneToOne
    @JoinColumn(nullable = false)
    private Person personId;

    @OneToMany(mappedBy = "roleId")
    private Set<RoleReview> roleReviewSet;

    @OneToMany(mappedBy = "roleId")
    private Set<RoleReviewCompliant> roleReviewCompliants;

    @OneToMany(mappedBy = "roleId")
    private Set<RoleReviewFeedback> roleReviewFeedbacks;

    @OneToMany(mappedBy = "roleId")
    private Set<RoleVote> roleVotes;
}
