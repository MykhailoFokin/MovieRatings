package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class MovieReadDTO {

    private UUID id;

    private String title;

    private Short year;

    private String description;

    private String soundMix;

    private String colour;

    private String aspectRatio;

    private String camera;

    private String laboratory;

    private String critique;

    private Boolean isPublished;

    private Instant createdAt;

    private Instant updatedAt;

    private Double averageRating;

    private Boolean adult;

    private String originalLanguage;

    private String originalTitle;

    private Long budget;

    private String homepage;

    private String imdbId;

    private Long revenue;

    private Integer runtime;

    private String tagline;
}
