package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieReview;

@Data
public class MovieSpoilerDataPutDTO {

    private MovieReview movieReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
