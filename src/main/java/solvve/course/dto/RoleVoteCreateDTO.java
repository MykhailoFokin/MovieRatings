package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserVoteRatingType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class RoleVoteCreateDTO {

    @NotNull
    private UUID portalUserId;

    @NotNull
    private UUID roleId;

    @NotNull
    private UserVoteRatingType rating;
}
