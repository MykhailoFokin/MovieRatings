package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RolePatchDTO {

    private String title;

    private String roleType;

    private String description;

    private UUID personId;
}
