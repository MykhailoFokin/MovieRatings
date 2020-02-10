package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
public class RoleReviewFeedback {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private PortalUser userId;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Role roleId;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private RoleReview roleReviewId;

    private Boolean isLiked;
}
