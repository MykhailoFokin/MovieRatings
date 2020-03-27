package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class RoleReviewFeedbackCreateDTO {

    @NotNull
    private UUID portalUserId;

    @NotNull
    private UUID roleId;

    @NotNull
    private UUID roleReviewId;

    private Boolean isLiked;
}
