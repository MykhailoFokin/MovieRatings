package solvve.course.dto;

public class MovieCreateDTO {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getFilmingLocations() {
        return filmingLocations;
    }

    public void setFilmingLocations(String filmingLocationsocations) {
        this.filmingLocations = filmingLocations;
    }

    public String getCritique() {
        return critique;
    }

    public void setCritique(String critique) {
        this.critique = critique;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieCreateDTO that = (MovieCreateDTO) o;

        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (year != null ? !year.equals(that.year) : that.year != null) return false;
        if (genres != null ? !genres.equals(that.genres) : that.genres != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (companies != null ? !companies.equals(that.companies) : that.companies != null) return false;
        if (soundMix != null ? !soundMix.equals(that.soundMix) : that.soundMix != null) return false;
        if (colour != null ? !colour.equals(that.colour) : that.colour != null) return false;
        if (aspectRatio != null ? !aspectRatio.equals(that.aspectRatio) : that.aspectRatio != null) return false;
        if (camera != null ? !camera.equals(that.camera) : that.camera != null) return false;
        if (laboratory != null ? !laboratory.equals(that.laboratory) : that.laboratory != null) return false;
        if (languages != null ? !languages.equals(that.languages) : that.languages != null) return false;
        if (filmingLocations != null ? !filmingLocations.equals(that.filmingLocations) : that.filmingLocations != null)
            return false;
        return critique != null ? critique.equals(that.critique) : that.critique == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (genres != null ? genres.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (companies != null ? companies.hashCode() : 0);
        result = 31 * result + (soundMix != null ? soundMix.hashCode() : 0);
        result = 31 * result + (colour != null ? colour.hashCode() : 0);
        result = 31 * result + (aspectRatio != null ? aspectRatio.hashCode() : 0);
        result = 31 * result + (camera != null ? camera.hashCode() : 0);
        result = 31 * result + (laboratory != null ? laboratory.hashCode() : 0);
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (filmingLocations != null ? filmingLocations.hashCode() : 0);
        result = 31 * result + (critique != null ? critique.hashCode() : 0);
        return result;
    }
}
