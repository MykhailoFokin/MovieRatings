package solvve.course.dto;

import java.util.UUID;

public class MovieVoteFeedbackReadDTO {

    private UUID id;

    private UUID userId;

    private UUID movieId;

    private UUID movieVoteId;

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
}
