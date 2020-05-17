package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class NewsFeedbackPutDTO {

    @NotNull
    private UUID portalUserId;

    @NotNull
    private UUID newsId;

    private Boolean isLiked;
}
