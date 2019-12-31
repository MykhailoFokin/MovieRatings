package solvve.course.dto;

import java.util.UUID;

public class RoleVoteFeedbackCreateDTO {

    private UUID userId;

    private UUID roleId;

    private UUID roleVoteId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleVoteFeedbackCreateDTO that = (RoleVoteFeedbackCreateDTO) o;

        if (isLiked != that.isLiked) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;
        return roleVoteId != null ? roleVoteId.equals(that.roleVoteId) : that.roleVoteId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + (roleVoteId != null ? roleVoteId.hashCode() : 0);
        result = 31 * result + (isLiked ? 1 : 0);
        return result;
    }
}
