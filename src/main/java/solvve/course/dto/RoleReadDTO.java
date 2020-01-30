package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleReadDTO {

    private UUID id;

    private String title;

    private String roleType;

    private String description;

    private UUID personId;
}
