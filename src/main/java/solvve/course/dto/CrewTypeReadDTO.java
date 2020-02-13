package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CrewTypeReadDTO {

    private UUID id;

    private String name;

    private UUID crew;

    private Instant createdAt;

    private Instant modifiedAt;
}
