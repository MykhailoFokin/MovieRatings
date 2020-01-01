package solvve.course.domain;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class MovieProdCountries {

    @EmbeddedId
    private MovieProdCountriesId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("countryId")
    private Countries countries;

    private MovieProdCountries() {}

    public MovieProdCountries(Movie movie, Countries countries) {
        this.movie = movie;
        this.countries = countries;
        this.id = new MovieProdCountriesId(movie.getId(), countries.getId());
    }

    public MovieProdCountriesId getId() {
        return id;
    }

    public void setId(MovieProdCountriesId id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Countries getCountries() {
        return countries;
    }

    public void setCountries(Countries countries) {
        this.countries = countries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieProdCountries that = (MovieProdCountries) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (movie != null ? !movie.equals(that.movie) : that.movie != null) return false;
        return countries != null ? countries.equals(that.countries) : that.countries == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (movie != null ? movie.hashCode() : 0);
        result = 31 * result + (countries != null ? countries.hashCode() : 0);
        return result;
    }
}
