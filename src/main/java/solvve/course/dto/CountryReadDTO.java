package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CountryReadDTO {

    private UUID id;

    private String name;

    private Instant createdAt;

    private Instant modifiedAt;
}
