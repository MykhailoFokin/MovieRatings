package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.util.Set;

@Data
public class MovieReviewPutDTO {

    private PortalUser userId;

    private Movie movieId;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private PortalUser moderatorId;

    private Set<MovieReviewCompliant> movieReviewCompliants;

    private Set<MovieReviewFeedback> movieReviewFeedbacks;

    private Set<MovieSpoilerData> movieSpoilerData;
}
