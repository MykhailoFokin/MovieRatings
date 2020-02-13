package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class RoleSpoilerDataReadDTO {

    private UUID id;

    private UUID roleReviewId;

    private Integer startIndex;

    private Integer endIndex;

    private Instant createdAt;

    private Instant modifiedAt;
}
