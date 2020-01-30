package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieReview;

import java.util.UUID;

@Data
public class MovieSpoilerDataCreateDTO {

    private UUID movieReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
