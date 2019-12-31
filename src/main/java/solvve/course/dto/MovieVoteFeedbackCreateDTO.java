package solvve.course.dto;

import java.util.UUID;

public class MovieVoteFeedbackCreateDTO {

    private UUID userId;

    private UUID movieId;

    private UUID movieVoteId;

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

    public UUID getMovieVoteId() {
        return movieVoteId;
    }

    public void setMovieVoteId(UUID movieVoteId) {
        this.movieVoteId = movieVoteId;
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
        if (o == null || getClass() != o.getClass()) return false;

        MovieVoteFeedbackCreateDTO that = (MovieVoteFeedbackCreateDTO) o;

        if (isLiked != that.isLiked) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (movieId != null ? !movieId.equals(that.movieId) : that.movieId != null) return false;
        return movieVoteId != null ? movieVoteId.equals(that.movieVoteId) : that.movieVoteId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (movieId != null ? movieId.hashCode() : 0);
        result = 31 * result + (movieVoteId != null ? movieVoteId.hashCode() : 0);
        result = 31 * result + (isLiked ? 1 : 0);
        return result;
    }
}
