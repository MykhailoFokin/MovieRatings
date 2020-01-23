package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class MovieReviewCompliant {

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

    private String description;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

    @ManyToOne
    @JoinColumn
    private PortalUser moderatorId;
}
