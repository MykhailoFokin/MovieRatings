package solvve.course.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class NewsReadDTO {

    private UUID id;

    private UUID userId;

    private Timestamp published;

    private String topic;

    private String description;
}
