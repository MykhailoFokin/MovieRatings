package solvve.course.domain;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class MovieProdCountries {

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
