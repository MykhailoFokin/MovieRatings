package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.RoleReview;

import java.util.UUID;

@Data
public class RoleSpoilerDataReadDTO {

    private UUID id;

    private RoleReview roleReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
