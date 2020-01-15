package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleSpoilerDataCreateDTO {

    private UUID roleReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
