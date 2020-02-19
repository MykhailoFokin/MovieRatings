package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserVoteRatingType;

import java.time.Instant;
import java.util.UUID;

@Data
public class RoleVoteReadDTO {

    private UUID id;

    private UUID userId;

    private UUID roleId;

    private UserVoteRatingType rating;

    private Instant createdAt;

    private Instant updatedAt;
}
