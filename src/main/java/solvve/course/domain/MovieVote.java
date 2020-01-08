package solvve.course.domain;

import solvve.course.dto.UserModeratedStatusType;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class MovieVote {

    @Id
    @GeneratedValue()
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
