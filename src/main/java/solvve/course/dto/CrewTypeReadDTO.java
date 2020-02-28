package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CrewTypeReadDTO {

    private UUID id;

    private String name;

    private UUID crewId;

    private Instant createdAt;

    private Instant updatedAt;
}
