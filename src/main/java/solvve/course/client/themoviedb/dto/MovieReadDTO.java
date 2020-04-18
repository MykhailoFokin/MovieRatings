package solvve.course.client.themoviedb.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieReadDTO {

    private String id;

    private String originalTitle;

    private String title;

    private Boolean adult;

    private String originalLanguage;

    private List<Integer> genreIds;

    private String overview;

    private String releaseDate;

    private Long budget;

    private String homepage;

    private String imdbId;

    private Long revenue;

    private Integer runtime;

    private String tagline;
}
