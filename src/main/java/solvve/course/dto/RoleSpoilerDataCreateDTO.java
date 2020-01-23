package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.RoleReview;

import java.util.UUID;

@Data
public class RoleSpoilerDataCreateDTO {

    private RoleReview roleReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
