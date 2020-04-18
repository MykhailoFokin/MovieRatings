package solvve.course.client.themoviedb.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieReadShortDTO {

    private String id;

    private String title;

    private String originalTitle;

    private Boolean adult;

    private String originalLanguage;

    private List<Integer> genreIds;

    private String overview;

    private String releaseDate;
}
