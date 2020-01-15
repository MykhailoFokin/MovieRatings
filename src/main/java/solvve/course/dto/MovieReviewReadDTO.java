package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import java.util.UUID;

@Data
public class MovieReviewReadDTO {

    private UUID id;

    private UUID userId;

    private UUID movieId;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;
}
