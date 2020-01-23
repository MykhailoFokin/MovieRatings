package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Movie;
import solvve.course.domain.MovieReview;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserModeratedStatusType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Data
public class MovieReviewCompliantReadDTO {

    private UUID id;

    private PortalUser userId;

    private Movie movieId;

    private MovieReview movieReviewId;

    private String description;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

    private PortalUser moderatorId;
}
