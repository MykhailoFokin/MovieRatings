package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
public class RoleReviewCompliant extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @NotNull
    private PortalUser portalUser;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @NotNull
    private Role role;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @NotNull
    private RoleReview roleReview;

    @Size(min = 1, max = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

    @ManyToOne
    @JoinColumn
    private PortalUser moderator;
}
