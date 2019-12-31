package solvve.course.dto;

import java.util.UUID;

public class MovieVoteCompliantCreateDTO {

    private UUID userId;

    private UUID movieId;

    private UUID movieVoteId;

    private String description;

    private String moderatedStatus;

    private UUID moderatorId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieVoteCompliantCreateDTO that = (MovieVoteCompliantCreateDTO) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (movieId != null ? !movieId.equals(that.movieId) : that.movieId != null) return false;
        if (movieVoteId != null ? !movieVoteId.equals(that.movieVoteId) : that.movieVoteId != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (moderatedStatus != null ? !moderatedStatus.equals(that.moderatedStatus) : that.moderatedStatus != null)
            return false;
        return moderatorId != null ? moderatorId.equals(that.moderatorId) : that.moderatorId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (movieId != null ? movieId.hashCode() : 0);
        result = 31 * result + (movieVoteId != null ? movieVoteId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (moderatedStatus != null ? moderatedStatus.hashCode() : 0);
        result = 31 * result + (moderatorId != null ? moderatorId.hashCode() : 0);
        return result;
    }
}
