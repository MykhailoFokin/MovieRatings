package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class NewsFeedbackReadDTO {

    private UUID id;

    private UUID portalUserId;

    private UUID newsId;

    private Boolean isLiked;

    private Instant createdAt;

    private Instant updatedAt;
}
