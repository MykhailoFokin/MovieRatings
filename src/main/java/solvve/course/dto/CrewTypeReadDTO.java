package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class CrewTypeReadDTO {

    private UUID id;

    private String name;

    private List<UUID> crewId;

    private Instant createdAt;

    private Instant updatedAt;
}
