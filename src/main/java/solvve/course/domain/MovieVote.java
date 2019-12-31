package solvve.course.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class MovieVote {

    @Id
    @GeneratedValue()
    private UUID id;

    private UUID userId;

    private UUID movieId;

    private int rating;

    private String description;

    private int spoilerStartIndex;

    private int spoilerEndIndex;

    private String moderatedStatus;

    private UUID moderatorId;

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSpoilerStartIndex() {
        return spoilerStartIndex;
    }

    public void setSpoilerStartIndex(int spoilerStartIndex) {
        this.spoilerStartIndex = spoilerStartIndex;
    }

    public int getSpoilerEndIndex() {
        return spoilerEndIndex;
    }

    public void setSpoilerEndIndex(int spoilerEndIndex) {
        this.spoilerEndIndex = spoilerEndIndex;
    }

    public String getModeratedStatus() {
        return moderatedStatus;
    }

    public void setModeratedStatus(String moderatedStatus) {
        this.moderatedStatus = moderatedStatus;
    }

    public UUID getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(UUID moderatorId) {
        this.moderatorId = moderatorId;
    }
}
