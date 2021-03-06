package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MovieCreateDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String title;

    @NotNull
    private Short year;

    @Size(min = 1, max = 1000)
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
}
