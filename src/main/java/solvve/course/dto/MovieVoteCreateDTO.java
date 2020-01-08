package solvve.course.dto;

import solvve.course.domain.UserVoteRatingType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

public class MovieVoteCreateDTO {

    private UUID userId;

    private UUID movieId;

    private int rating;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getMovieId() {
        return movieId;
    }

    public void setMovieId(UUID movieId) {
        this.movieId = movieId;
    }

    public UserVoteRatingType getRating() { return UserVoteRatingType.parse(this.rating); }

    public void setRating(UserVoteRatingType rating) {
        this.rating = rating.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieVoteCreateDTO)) return false;

        MovieVoteCreateDTO that = (MovieVoteCreateDTO) o;

        if (rating != that.rating) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return movieId != null ? movieId.equals(that.movieId) : that.movieId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (movieId != null ? movieId.hashCode() : 0);
        result = 31 * result + rating;
        return result;
    }
}
