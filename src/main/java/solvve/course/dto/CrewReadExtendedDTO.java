package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CrewReadExtendedDTO {

    private UUID id;

    private PersonReadDTO personId;

    private MovieReadDTO movieId;

    private CrewTypeReadDTO crewTypeId;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;
}
