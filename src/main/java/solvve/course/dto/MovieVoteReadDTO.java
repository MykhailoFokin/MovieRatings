package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserVoteRatingType;

import java.time.Instant;
import java.util.UUID;

@Data
public class MovieVoteReadDTO {

    private UUID id;

    private UUID userId;

    private UUID movieId;

    private UserVoteRatingType rating;

    private Instant createdAt;

    private Instant updatedAt;
}
