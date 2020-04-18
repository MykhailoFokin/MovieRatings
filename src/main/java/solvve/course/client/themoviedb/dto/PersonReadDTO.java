package solvve.course.client.themoviedb.dto;

import lombok.Data;

@Data
public class PersonReadDTO {

    private String id;

    private String name;

    private String birthday;

    private String knownForDepartment;

    private String deathday;

    private Short gender;

    private String biography;

    private String placeOfBirth;

    private Boolean adult;

    private String imdbId;

    private String homepage;
}
