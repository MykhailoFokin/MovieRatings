package solvve.course.dto;

import java.util.UUID;

public class RoleReviewFeedbackReadDTO {

    private UUID id;

    private UUID userId;

    private UUID roleId;

    private UUID roleReviewId;

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
}
