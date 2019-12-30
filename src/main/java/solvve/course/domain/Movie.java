package solvve.course.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Movie {

    @Id
    @GeneratedValue()
    private UUID id;

    private String title;

    private Short year; // year of production

    private String genres; // type of genres

    private String releaseDates; // different release dates depending on country

    private String description; // short movie description

    // Movie Crew (one entity for link between entity crew (its list of people) and movie itself
    // because movie related to crew as many to many we need entity between them
    // possible types:
    /*directors
    writers
    producers
    musicby
    cinematographyby
    operators
    specialeffectscrew
    othercrew*/

    private String crew;

    // Company Credits
    // Same as Crew. Entity Companies and Entity for linkage
    // possible types:
    /*Production Companies
    Distributors
    Special Effects
    Other Companies*/
    private String companies;

    // Technical Specifications
    // it will be dictionaries of appropriate information
    private String soundMix; // sound technologies used in movie
    private String colour; // optional for color scheme
    private String aspectRatio; // examples: 1.43 : 1 (some scenes: 70mm IMAX); 1.90 : 1 (some scenes: Digital IMAX); 2.39 : 1;
    private String camera; // example: Arriflex 235, Panavision Primo Lenses; Arriflex 435, Panavision Primo Lenses; IMAX MSM 9802, Hasselblad Lenses (some scenes)
    private String laboratory; // post production companies, etc

    // Details
    private String countries; // production by
    private String languages; // original production language
    private String filmingLocations; // many-to-many. addresses to movies.

    private String critique; // movie 1-8 critique 1-1(1-8) crew

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

    public String getFilmingLocations() {
        return filmingLocations;
    }

    public void setFilmingLocations(String filmingLocations) {
        this.filmingLocations = filmingLocations;
    }

    public String getCritique() {
        return critique;
    }

    public void setCritique(String critique) {
        this.critique = critique;
    }
}
