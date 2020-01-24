package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieReview;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserModeratedStatusType;

@Data
public class MovieReviewCompliantPutDTO {

    private PortalUser userId;

    private Movie movieId;

    private MovieReview movieReviewId;

    private String description;

    private UserModeratedStatusType moderatedStatus;

    private PortalUser moderatorId;
}
