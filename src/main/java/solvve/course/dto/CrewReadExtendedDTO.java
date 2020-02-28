package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CrewReadExtendedDTO {

    private UUID id;

    private PersonReadDTO person;

    private MovieReadDTO movie;

    private CrewTypeReadDTO crewType;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;
}
