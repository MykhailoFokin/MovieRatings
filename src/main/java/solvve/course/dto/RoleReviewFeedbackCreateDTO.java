package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleReviewFeedbackCreateDTO {

    private UUID portalUserId;

    private UUID roleId;

    private UUID roleReviewId;

    private Boolean isLiked;
}
