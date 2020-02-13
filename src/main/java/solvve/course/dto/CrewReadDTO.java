package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CrewReadDTO {

    private UUID id;

    private UUID personId;

    private UUID movieId;

    private UUID crewType;

    private String description;

    private Instant createdAt;

    private Instant modifiedAt;
}
