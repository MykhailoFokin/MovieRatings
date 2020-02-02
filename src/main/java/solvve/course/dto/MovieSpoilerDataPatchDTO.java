package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MovieSpoilerDataPatchDTO {

    private UUID movieReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
