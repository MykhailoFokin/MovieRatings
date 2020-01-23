package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class MovieReview {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private PortalUser userId;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movieId;

    private String textReview;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

    @ManyToOne
    @JoinColumn(name = "moderator_id", referencedColumnName = "id")
    private PortalUser moderatorId;

    @OneToMany(mappedBy = "movieReviewId")
    private Set<MovieReviewCompliant> movieReviewCompliants;

    @OneToMany(mappedBy = "movieReviewId")
    private Set<MovieReviewFeedback> movieReviewFeedbacks;

    @OneToMany(mappedBy = "movieReviewId")
    private Set<MovieSpoilerData> movieSpoilerData;
}
