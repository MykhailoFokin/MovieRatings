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
public class RoleReview extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @NotNull
    private PortalUser portalUser;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @NotNull
    private Role role;

    @Size(min = 1, max = 1000)
    private String textReview;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

    @ManyToOne
    @JoinColumn
    private PortalUser moderator;

    @OneToMany(mappedBy = "roleReview", cascade = CascadeType.PERSIST)
    private Set<RoleReviewCompliant> roleReviewCompliants;

    @OneToMany(mappedBy = "roleReview", cascade = CascadeType.PERSIST)
    private Set<RoleReviewFeedback> roleReviewFeedbacks;

    @OneToMany(mappedBy = "roleReview", cascade = CascadeType.PERSIST)
    private Set<RoleSpoilerData> roleSpoilerData;
}
