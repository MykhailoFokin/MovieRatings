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

    @OneToMany(mappedBy = "roleReviewId", cascade = CascadeType.PERSIST)
    private Set<RoleReviewCompliant> roleReviewCompliants;

    @OneToMany(mappedBy = "roleReviewId", cascade = CascadeType.PERSIST)
    private Set<RoleReviewFeedback> roleReviewFeedbacks;

    @OneToMany(mappedBy = "roleReviewId", cascade = CascadeType.PERSIST)
    private Set<RoleSpoilerData> roleSpoilerData;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
