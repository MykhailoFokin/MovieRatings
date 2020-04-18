package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class MoviePutDTO {

    @Size(min = 11, max = 1000)
    private String description;

    @Size(min = 1, max = 255)
    private String soundMix;

    @Size(min = 1, max = 255)
    private String colour;

    @Size(min = 1, max = 255)
    private String aspectRatio;

    @Size(min = 1, max = 255)
    private String camera;

    @Size(min = 1, max = 255)
    private String laboratory;

    @Size(min = 1, max = 255)
    private String critique;

    private Boolean isPublished;

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
