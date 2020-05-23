package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

@Data
public class NewsPutDTO {

    private UUID publisherId;

    private Instant published;

    @NotNull
    @Size(min = 1, max = 255)
    private String topic;

    @NotNull
    @Size(min = 1, max = 1000)
    private String description;

    private UUID movieId;
}
