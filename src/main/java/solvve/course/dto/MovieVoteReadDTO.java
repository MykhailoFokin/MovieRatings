package solvve.course.dto;

import solvve.course.domain.UserVoteRatingType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

public class MovieVoteReadDTO {

    private UUID id;

    private UUID userId;

    private UUID movieId;

    private int rating;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
}
