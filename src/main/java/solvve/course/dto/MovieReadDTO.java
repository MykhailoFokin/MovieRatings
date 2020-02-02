package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MovieReadDTO {

    private UUID id;

    private String title;

    private Short year;

    private String genres;

    private String description;

    private String companies;

    private String soundMix;

    private String colour;

    private String aspectRatio;

    private String camera;

    private String laboratory;

    private String languages;

    private String filmingLocations;

    private String critique;

    private Boolean isPublished;
}
