package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

@Data
public class UserTypoRequestCreateDTO {

    @NotNull
    private UUID requesterId;

    private Instant fixAppliedDate;

    @Size(min = 1, max = 1000)
    private String approvedText;

    private UUID movieId;

    private UUID newsId;

    private UUID roleId;
}
