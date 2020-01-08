package solvve.course.dto;

import java.util.UUID;

public class MovieReviewFeedbackCreateDTO {

    private UUID userId;

    private UUID movieId;

    private UUID movieReviewId;

    private boolean isLiked;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieReviewFeedbackCreateDTO)) return false;

        MovieReviewFeedbackCreateDTO that = (MovieReviewFeedbackCreateDTO) o;

        if (isLiked != that.isLiked) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (movieId != null ? !movieId.equals(that.movieId) : that.movieId != null) return false;
        return movieReviewId != null ? movieReviewId.equals(that.movieReviewId) : that.movieReviewId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (movieId != null ? movieId.hashCode() : 0);
        result = 31 * result + (movieReviewId != null ? movieReviewId.hashCode() : 0);
        result = 31 * result + (isLiked ? 1 : 0);
        return result;
    }
}
