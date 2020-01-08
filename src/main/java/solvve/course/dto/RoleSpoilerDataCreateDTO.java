package solvve.course.dto;

import java.util.UUID;

public class RoleSpoilerDataCreateDTO {

    private UUID roleReviewId;

    private int startIndex;

    private int endIndex;

    public UUID getRoleReviewId() {
        return roleReviewId;
    }

    public void setRoleReviewId(UUID roleReviewId) {
        this.roleReviewId = roleReviewId;
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
        if (!(o instanceof RoleSpoilerDataCreateDTO)) return false;

        RoleSpoilerDataCreateDTO that = (RoleSpoilerDataCreateDTO) o;

        if (startIndex != that.startIndex) return false;
        if (endIndex != that.endIndex) return false;
        return roleReviewId != null ? roleReviewId.equals(that.roleReviewId) : that.roleReviewId == null;
    }

    @Override
    public int hashCode() {
        int result = roleReviewId != null ? roleReviewId.hashCode() : 0;
        result = 31 * result + startIndex;
        result = 31 * result + endIndex;
        return result;
    }
}
