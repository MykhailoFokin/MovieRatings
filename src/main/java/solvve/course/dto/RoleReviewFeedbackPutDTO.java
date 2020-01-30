package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleReviewFeedbackPutDTO {

    private UUID userId;

    private UUID roleId;

    private UUID roleReviewId;

    private Boolean isLiked;
}
