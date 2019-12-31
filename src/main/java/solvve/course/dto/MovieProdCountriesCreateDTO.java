package solvve.course.dto;

import java.util.UUID;

public class MovieProdCountriesCreateDTO {

    private UUID movieId;

    private UUID countryId;

    public UUID getMovieId() {
        return movieId;
    }

    public void setMovieId(UUID movieId) {
        this.movieId = movieId;
    }

    public UUID getCountryId() {
        return countryId;
    }

    public void setCountryId(UUID countryId) {
        this.countryId = countryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieProdCountriesCreateDTO that = (MovieProdCountriesCreateDTO) o;

        if (movieId != null ? !movieId.equals(that.movieId) : that.movieId != null) return false;
        return countryId != null ? countryId.equals(that.countryId) : that.countryId == null;
    }

    @Override
    public int hashCode() {
        int result = movieId != null ? movieId.hashCode() : 0;
        result = 31 * result + (countryId != null ? countryId.hashCode() : 0);
        return result;
    }
}
