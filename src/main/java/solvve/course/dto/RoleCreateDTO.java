package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.RoleType;

import java.util.UUID;

@Data
public class RoleCreateDTO {

    private String title;

    private RoleType roleType;

    private String description;

    private UUID personId;

    private UUID movieId;
}
