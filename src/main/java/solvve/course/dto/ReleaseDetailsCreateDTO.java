package solvve.course.dto;

import java.sql.Date;
import java.util.UUID;

public class ReleaseDetailsCreateDTO {

    private UUID movieId;

    private Date releaseDate;

    private UUID countryId;

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public UUID getCountryId() {
        return countryId;
    }

    public void setCountryId(UUID countryId) {
        this.countryId = countryId;
    }

    public UUID getMovieId() {
        return movieId;
    }

    public void setMovieId(UUID movieId) {
        this.movieId = movieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReleaseDetailsCreateDTO that = (ReleaseDetailsCreateDTO) o;

        if (movieId != null ? !movieId.equals(that.movieId) : that.movieId != null) return false;
        if (releaseDate != null ? !releaseDate.equals(that.releaseDate) : that.releaseDate != null) return false;
        return countryId != null ? countryId.equals(that.countryId) : that.countryId == null;
    }

    @Override
    public int hashCode() {
        int result = movieId != null ? movieId.hashCode() : 0;
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (countryId != null ? countryId.hashCode() : 0);
        return result;
    }
}
