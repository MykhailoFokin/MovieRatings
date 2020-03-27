package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class RoleSpoilerDataPutDTO {

    @NotNull
    private UUID roleReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
