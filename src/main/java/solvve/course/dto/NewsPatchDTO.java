package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.PortalUser;

import java.time.Instant;
import java.util.UUID;

@Data
public class NewsPatchDTO {

    private UUID userId;

    private Instant published;

    private String topic;

    private String description;
}
