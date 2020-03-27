package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Entity
public class NewsUserReview extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "portal_user_id", referencedColumnName = "id")
    @NotNull
    private PortalUser portalUser;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    @NotNull
    private News news;

    @Enumerated(EnumType.STRING)
    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    @ManyToOne
    @JoinColumn(name = "moderator_id", referencedColumnName = "id")
    private PortalUser moderator;

    @OneToMany(mappedBy = "newsUserReview", cascade = CascadeType.PERSIST)
    private Set<NewsUserReviewNote> newsUserReviewNotes;
}
