package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class RoleReviewFeedback extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private PortalUser portalUser;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private RoleReview roleReview;

    private Boolean isLiked;
}
