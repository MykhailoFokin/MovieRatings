package solvve.course.domain;

import solvve.course.dto.UserModeratedStatusType;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class MovieReviewCompliant {

    @Id
    @GeneratedValue()
    private UUID id;

    private UUID userId;

    private UUID movieId;

    private UUID movieReviewId;

    private String description;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

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

    public UUID getMovieReviewId() {
        return movieReviewId;
    }

    public void setMovieReviewId(UUID movieReviewId) {
        this.movieReviewId = movieReviewId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserModeratedStatusType getModeratedStatus() {
        return moderatedStatus;
    }

    public void setModeratedStatus(UserModeratedStatusType moderatedStatus) {
        this.moderatedStatus = moderatedStatus;
    }

    public UUID getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(UUID moderatorId) {
        this.moderatorId = moderatorId;
    }
}
