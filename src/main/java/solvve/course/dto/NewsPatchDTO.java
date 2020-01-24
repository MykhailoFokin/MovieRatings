package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.PortalUser;

import java.time.Instant;

@Data
public class NewsPatchDTO {

    private PortalUser userId;

    private Instant published;

    private String topic;

    private String description;
}
