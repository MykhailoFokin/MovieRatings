package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.PortalUser;
import solvve.course.domain.Role;
import solvve.course.domain.UserVoteRatingType;

import java.util.UUID;

@Data
public class RoleVoteReadDTO {

    private UUID id;

    private PortalUser userId;

    private Role roleId;

    private UserVoteRatingType rating;
}
