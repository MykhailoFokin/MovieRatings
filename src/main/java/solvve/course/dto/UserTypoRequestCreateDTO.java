package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class UserTypoRequestCreateDTO {

    private UUID requesterId;

    private Instant fixAppliedDate;

    private String approvedText;

    private UUID movieId;

    private UUID newsId;

    private UUID roleId;
}
