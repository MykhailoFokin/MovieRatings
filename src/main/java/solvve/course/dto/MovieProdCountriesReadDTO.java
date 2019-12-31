package solvve.course.dto;

import java.util.UUID;

public class MovieProdCountriesReadDTO {

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
}
