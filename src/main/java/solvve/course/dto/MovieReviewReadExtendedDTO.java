package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class MovieReviewReadExtendedDTO {

    private UUID id;

    private PortalUser portalUser;

    private Movie movie;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private PortalUser moderator;

    private List<MovieReviewCompliant> movieReviewCompliants;

    private List<MovieReviewFeedback> movieReviewFeedbacks;

    private List<MovieSpoilerData> movieSpoilerData;

    private Instant createdAt;

    private Instant updatedAt;
}
