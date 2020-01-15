package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.UserModeratedStatusType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Data
public class MovieReviewCompliantCreateDTO {

    private UUID userId;

    private UUID movieId;

    private UUID movieReviewId;

    private String description;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;
}
