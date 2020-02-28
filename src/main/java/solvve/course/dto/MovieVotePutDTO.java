package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserVoteRatingType;

import java.util.UUID;

@Data
public class MovieVotePutDTO {

    private UUID portalUserId;

    private UUID movieId;

    private UserVoteRatingType rating;
}
