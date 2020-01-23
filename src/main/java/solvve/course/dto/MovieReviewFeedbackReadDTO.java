package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieReview;
import solvve.course.domain.PortalUser;

import java.util.UUID;

@Data
public class MovieReviewFeedbackReadDTO {

    private UUID id;

    private PortalUser userId;

    private Movie movieId;

    private MovieReview movieReviewId;

    private Boolean isLiked;
}
