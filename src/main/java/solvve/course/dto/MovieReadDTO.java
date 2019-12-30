package solvve.course.dto;

import java.util.UUID;

public class MovieReadDTO {

    private UUID id;

    private String title;

    private Short year;

    private String genres;

    private String releaseDates;

    private String description;

    private String crew;

    private String companies;

    private String soundMix;

    private String colour;

    private String aspectRatio;

    private String camera;

    private String laboratory;

    private String countries;

    private String languages;

    private String filmingLocations;

    private String critique;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getReleaseDates() {
        return releaseDates;
    }

    public void setReleaseDates(String releaseDates) {
        this.releaseDates = releaseDates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCrew() {
        return crew;
    }

    public void setCrew(String crew) {
        this.crew = crew;
    }

    public String getCompanies() {
        return companies;
    }

    public void setCompanies(String companies) {
        this.companies = companies;
    }

    public String getSoundMix() {
        return soundMix;
    }

    public void setSoundMix(String soundMix) {
        this.soundMix = soundMix;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getFilminglocations() {
        return filmingLocations;
    }

    public void setFilminglocations(String filmingLocationsocations) {
        this.filmingLocations = filmingLocations;
    }

    public String getCritique() {
        return critique;
    }

    public void setCritique(String critique) {
        this.critique = critique;
    }
}
