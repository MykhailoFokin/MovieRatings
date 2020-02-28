package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserVoteRatingType;

import java.util.UUID;

@Data
public class MovieVotePatchDTO {

    private UUID portalUserId;

    private UUID movieId;

    private UserVoteRatingType rating;
}
