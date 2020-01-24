package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieReview;
import solvve.course.domain.PortalUser;

@Data
public class MovieReviewFeedbackPutDTO {

    private PortalUser userId;

    private Movie movieId;

    private MovieReview movieReviewId;

    private Boolean isLiked;
}
