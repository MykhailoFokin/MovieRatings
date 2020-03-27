package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserVoteRatingType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class MovieVoteCreateDTO {

    @NotNull
    private UUID portalUserId;

    @NotNull
    private UUID movieId;

    @NotNull
    private UserVoteRatingType rating;
}
