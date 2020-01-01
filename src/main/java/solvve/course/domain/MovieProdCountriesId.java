package solvve.course.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class MovieProdCountriesId implements Serializable {

    private UUID movieId;

    private UUID countryId;

    private MovieProdCountriesId() {}

    public MovieProdCountriesId(UUID movieId,UUID countryId) {
        this.countryId = countryId;
        this.movieId = movieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieProdCountriesId that = (MovieProdCountriesId) o;

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
