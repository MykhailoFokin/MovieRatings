package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class NewsFeedbackCreateDTO {

    @NotNull
    private UUID portalUserId;

    @NotNull
    private UUID newsId;

    private Boolean isLiked;
}
