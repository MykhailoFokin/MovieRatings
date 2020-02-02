package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MovieSpoilerDataReadDTO {

    private UUID id;

    private UUID movieReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
