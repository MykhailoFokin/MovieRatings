package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class MovieReviewCreateDTO {

    @NotNull
    private UUID portalUserId;

    @NotNull
    private UUID movieId;

    @NotNull
    @Size(min = 1, max = 1000)
    private String textReview;

    private UUID moderatorId;
}
