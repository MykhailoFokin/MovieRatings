package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class RoleReviewCompliant {

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

    private String description;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

    @ManyToOne
    @JoinColumn
    private PortalUser moderatorId;
}
