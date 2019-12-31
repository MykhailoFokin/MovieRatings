package solvve.course.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class MovieVoteCompliant {

    @Id
    @GeneratedValue()
    private UUID id;

    private UUID userId;

    private UUID movieId;

    private UUID movieVoteId;

    private String description;

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

    public UUID getMovieVoteId() {
        return movieVoteId;
    }

    public void setMovieVoteId(UUID movieVoteId) {
        this.movieVoteId = movieVoteId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
