package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class MovieReviewReadExtendedDTO {

    private UUID id;

    private PortalUser userId;

    private Movie movieId;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private PortalUser moderatorId;

    private Set<MovieReviewCompliant> movieReviewCompliants;

    private Set<MovieReviewFeedback> movieReviewFeedbacks;

    private Set<MovieSpoilerData> movieSpoilerData;

    private Instant createdAt;

    private Instant modifiedAt;
}
