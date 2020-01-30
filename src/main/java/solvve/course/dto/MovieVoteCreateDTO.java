package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Movie;
import solvve.course.domain.PortalUser;
import solvve.course.domain.UserVoteRatingType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Data
public class MovieVoteCreateDTO {

    private UUID userId;

    private UUID movieId;

    private UserVoteRatingType rating;
}
