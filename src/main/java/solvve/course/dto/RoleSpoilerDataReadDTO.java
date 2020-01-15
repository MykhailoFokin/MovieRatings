package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleSpoilerDataReadDTO {

    private UUID id;

    private UUID roleReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
