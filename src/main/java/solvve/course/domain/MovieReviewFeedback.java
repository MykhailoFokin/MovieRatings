package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class MovieReviewFeedback extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @NotNull
    private PortalUser portalUser;

    @ManyToOne
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @NotNull
    private MovieReview movieReview;

    private Boolean isLiked;
}
