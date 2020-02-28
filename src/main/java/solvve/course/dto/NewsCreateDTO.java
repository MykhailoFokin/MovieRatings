package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class NewsCreateDTO {

    private UUID publisherId;

    private Instant published;

    private String topic;

    private String description;
}
