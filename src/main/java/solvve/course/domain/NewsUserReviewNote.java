package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
public class NewsUserReviewNote extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "moderator_id", referencedColumnName = "id")
    private PortalUser moderator;

    @NotNull
    @Size(min = 1, max = 1000)
    private String proposedText;

    @Size(min = 1, max = 1000)
    private String approvedText;

    @NotNull
    @Size(min = 1, max = 1000)
    private String sourceText;

    @NotNull
    private Integer startIndex;

    @NotNull
    private Integer endIndex;

    @Enumerated(EnumType.STRING)
    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    @ManyToOne
    @JoinColumn(name = "news_user_review_id", referencedColumnName = "id")
    @NotNull
    private NewsUserReview newsUserReview;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    @NotNull
    private News news;
}
