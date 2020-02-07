package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.LanguageType;
import solvve.course.domain.MovieGenreType;
import solvve.course.domain.MovieProductionType;

import java.util.List;

@Data
public class MovieFilter {

    private String title;

    private Short year;

    private List<MovieGenreType> genres;

    private String description;

    private MovieProductionType companyType;

    private String companyName;

    private String soundMix;

    private String colour;

    private String aspectRatio;

    private String camera;

    private String laboratory;

    private LanguageType language;

    private Boolean isPublished;

    private List<String> titles;

    private List<Short> years;

    private List<MovieProductionType> companyTypes;

    private List<LanguageType> languages;
}
