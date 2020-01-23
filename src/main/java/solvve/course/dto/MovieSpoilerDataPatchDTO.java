package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieReview;

import java.util.UUID;

@Data
public class MovieSpoilerDataPatchDTO {

    private MovieReview movieReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
