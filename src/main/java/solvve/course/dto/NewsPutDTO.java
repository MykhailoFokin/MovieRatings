package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class NewsPutDTO {

    private UUID publisher;

    private Instant published;

    private String topic;

    private String description;
}
