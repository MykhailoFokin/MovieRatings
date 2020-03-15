package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class NewsUserReviewNote {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "moderator_id", referencedColumnName = "id")
    private PortalUser moderator;

    private String proposedText;

    private String approvedText;

    @NotNull
    private String sourceText;

    @NotNull
    private Integer startIndex;

    @NotNull
    private Integer endIndex;

    @Enumerated(EnumType.STRING)
    private NewsUserReviewStatusType newsUserReviewStatusType;

    @ManyToOne
    @JoinColumn(name = "news_user_review_id", referencedColumnName = "id")
    private NewsUserReview newsUserReview;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News news;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
