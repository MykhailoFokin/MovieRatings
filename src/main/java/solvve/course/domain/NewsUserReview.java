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
public class NewsUserReview {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "portal_user_id", referencedColumnName = "id")
    private PortalUser portalUser;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News news;

    @Enumerated(EnumType.STRING)
    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    @ManyToOne
    @JoinColumn(name = "moderator_id", referencedColumnName = "id")
    private PortalUser moderator;

    @OneToMany(mappedBy = "newsUserReview", cascade = CascadeType.PERSIST)
    private Set<NewsUserReviewNote> newsUserReviewNotes;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
