package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.*;

import java.util.UUID;

@Data
public class MovieReviewCreateDTO {

    private UUID userId;

    private UUID movieId;

    private String textReview;

    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;
}
