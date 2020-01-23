package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class RoleReview {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private PortalUser userId;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Role roleId;

    private String textReview;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

    @ManyToOne
    @JoinColumn
    private PortalUser moderatorId;

    @OneToMany(mappedBy = "roleReviewId")
    private Set<RoleReviewCompliant> roleReviewCompliants;

    @OneToMany(mappedBy = "roleReviewId")
    private Set<RoleReviewFeedback> roleReviewFeedbacks;

    @OneToMany(mappedBy = "roleReviewId")
    private Set<RoleSpoilerData> roleSpoilerData;
}
