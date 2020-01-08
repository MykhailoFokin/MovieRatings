package solvve.course.dto;

import java.util.UUID;

public class MovieReviewFeedbackReadDTO {

    private UUID id;

    private UUID userId;

    private UUID movieId;

    private UUID movieReviewId;

    private boolean isLiked;

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

    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean liked) {
        isLiked = liked;
    }
}
