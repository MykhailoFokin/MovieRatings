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

    private String roleType; // main, second, etc

    private String description;

    @OneToOne
    @JoinColumn
    private Person personId;

    @OneToMany(mappedBy = "roleId", cascade = CascadeType.PERSIST)
    private Set<RoleReview> roleReviewSet;

    @OneToMany(mappedBy = "roleId", cascade = CascadeType.PERSIST)
    private Set<RoleReviewCompliant> roleReviewCompliants;

    @OneToMany(mappedBy = "roleId", cascade = CascadeType.PERSIST)
    private Set<RoleReviewFeedback> roleReviewFeedbacks;

    @OneToMany(mappedBy = "roleId", cascade = CascadeType.PERSIST)
    private Set<RoleVote> roleVotes;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;
}
