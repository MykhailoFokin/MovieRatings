package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.RoleType;

import java.time.Instant;
import java.util.UUID;

@Data
public class RoleReadDTO {

    private UUID id;

    private String title;

    private RoleType roleType;

    private String description;

    private UUID personId;

    private UUID movieId;

    private Instant createdAt;

    private Instant updatedAt;
}
