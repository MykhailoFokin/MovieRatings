package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
public class MovieReviewFeedback {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private PortalUser userId;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Movie movieId;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private MovieReview movieReviewId;

    private Boolean isLiked;
}
