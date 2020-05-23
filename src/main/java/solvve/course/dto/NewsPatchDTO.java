package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

@Data
public class NewsPatchDTO {

    private UUID publisherId;

    private Instant published;

    @Size(min = 1, max = 255)
    private String topic;

    @Size(min = 1, max = 1000)
    private String description;

    private UUID movieId;
}
