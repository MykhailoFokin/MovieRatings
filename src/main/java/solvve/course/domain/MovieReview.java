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

    @OneToMany(mappedBy = "movieReviewId", cascade = CascadeType.PERSIST)
    private Set<MovieReviewCompliant> movieReviewCompliants;

    @OneToMany(mappedBy = "movieReviewId", cascade = CascadeType.PERSIST)
    private Set<MovieReviewFeedback> movieReviewFeedbacks;

    @OneToMany(mappedBy = "movieReviewId", cascade = CascadeType.PERSIST)
    private Set<MovieSpoilerData> movieSpoilerData;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
