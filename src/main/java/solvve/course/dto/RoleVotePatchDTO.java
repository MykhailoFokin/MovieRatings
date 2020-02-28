package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserVoteRatingType;

import java.util.UUID;

@Data
public class RoleVotePatchDTO {

    private UUID portalUserId;

    private UUID roleId;

    private UserVoteRatingType rating;
}
