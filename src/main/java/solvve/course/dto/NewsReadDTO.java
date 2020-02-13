package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class NewsReadDTO {

    private UUID id;

    private UUID userId;

    private Instant published;

    private String topic;

    private String description;

    private Instant createdAt;

    private Instant modifiedAt;
}
