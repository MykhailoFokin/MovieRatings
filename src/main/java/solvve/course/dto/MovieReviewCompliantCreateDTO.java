package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import java.util.UUID;

@Data
public class MovieReviewCompliantCreateDTO {

    private UUID portalUserId;

    private UUID movieId;

    private UUID movieReviewId;

    private String description;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;
}
