package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class RoleReviewFeedback extends AbstractEntity {

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

    private Boolean isLiked;
}
