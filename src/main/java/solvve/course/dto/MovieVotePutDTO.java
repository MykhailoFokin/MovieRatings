package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Movie;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserVoteRatingType;

@Data
public class MovieVotePutDTO {

    private PortalUser userId;

    private Movie movieId;

    private UserVoteRatingType rating;
}
