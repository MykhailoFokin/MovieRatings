package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class MovieSpoilerDataPutDTO {

    @NotNull
    private UUID movieReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
