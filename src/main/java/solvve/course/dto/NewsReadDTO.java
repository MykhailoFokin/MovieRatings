package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class NewsReadDTO {

    private UUID id;

    private UUID publisherId;

    private Instant published;

    private String topic;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private UUID movieId;

    private Integer likesCount;

    private Double newsRating;

    private Integer dislikesCount;
}
