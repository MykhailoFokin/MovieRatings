package solvve.course.dto;

import java.util.UUID;

public class MovieSpoilerDataCreateDTO {

    private UUID movieReviewId;

    private int startIndex;

    private int endIndex;

    public UUID getMovieReviewId() {
        return movieReviewId;
    }

    public void setMovieReviewId(UUID movieReviewId) {
        this.movieReviewId = movieReviewId;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieSpoilerDataCreateDTO)) return false;

        MovieSpoilerDataCreateDTO that = (MovieSpoilerDataCreateDTO) o;

        if (startIndex != that.startIndex) return false;
        if (endIndex != that.endIndex) return false;
        return movieReviewId != null ? movieReviewId.equals(that.movieReviewId) : that.movieReviewId == null;
    }

    @Override
    public int hashCode() {
        int result = movieReviewId != null ? movieReviewId.hashCode() : 0;
        result = 31 * result + startIndex;
        result = 31 * result + endIndex;
        return result;
    }
}
