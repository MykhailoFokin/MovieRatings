package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.util.UUID;

@Data
public class RoleReviewPatchDTO {

    private UUID userId;

    private UUID roleId;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;
}
