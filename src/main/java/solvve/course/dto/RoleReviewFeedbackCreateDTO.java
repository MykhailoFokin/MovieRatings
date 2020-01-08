package solvve.course.dto;

import java.util.UUID;

public class RoleReviewFeedbackCreateDTO {

    private UUID userId;

    private UUID roleId;

    private UUID roleReviewId;

    private boolean isLiked;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public UUID getRoleReviewId() {
        return roleReviewId;
    }

    public void setRoleReviewId(UUID roleReviewId) {
        this.roleReviewId = roleReviewId;
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
        if (!(o instanceof RoleReviewFeedbackCreateDTO)) return false;

        RoleReviewFeedbackCreateDTO that = (RoleReviewFeedbackCreateDTO) o;

        if (isLiked != that.isLiked) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;
        return roleReviewId != null ? roleReviewId.equals(that.roleReviewId) : that.roleReviewId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + (roleReviewId != null ? roleReviewId.hashCode() : 0);
        result = 31 * result + (isLiked ? 1 : 0);
        return result;
    }
}
