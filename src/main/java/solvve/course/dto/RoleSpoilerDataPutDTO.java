package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleSpoilerDataPutDTO {

    private UUID roleReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
