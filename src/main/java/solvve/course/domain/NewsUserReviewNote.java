package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class NewsUserReviewNote extends AbstractEntity {

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
    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    @ManyToOne
    @JoinColumn(name = "news_user_review_id", referencedColumnName = "id")
    private NewsUserReview newsUserReview;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News news;
}
