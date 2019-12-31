package solvve.course.dto;

import java.util.UUID;

public class RoleVoteFeedbackReadDTO {

    private UUID id;

    private UUID userId;

    private UUID roleId;

    private UUID roleVoteId;

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

    public UUID getRoleVoteId() {
        return roleVoteId;
    }

    public void setRoleVoteId(UUID roleVoteId) {
        this.roleVoteId = roleVoteId;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean liked) {
        isLiked = liked;
    }
}
