package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.PortalUser;
import solvve.course.domain.Role;
import solvve.course.domain.UserVoteRatingType;

@Data
public class RoleVotePutDTO {

    private PortalUser userId;

    private Role roleId;

    private UserVoteRatingType rating;
}
