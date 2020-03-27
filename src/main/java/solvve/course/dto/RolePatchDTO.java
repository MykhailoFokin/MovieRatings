package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.RoleType;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class RolePatchDTO {

    private String title;

    private RoleType roleType;

    @Size(min = 1, max = 1000)
    private String description;

    private UUID personId;

    private UUID movieId;
}
