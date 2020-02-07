package solvve.course.dto;

import lombok.Data;

@Data
public class MoviePatchDTO {

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
}
