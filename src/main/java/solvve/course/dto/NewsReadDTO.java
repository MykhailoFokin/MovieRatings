package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.PortalUser;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class NewsReadDTO {

    private UUID id;

    private PortalUser userId;

    private Timestamp published;

    private String topic;

    private String description;
}
