package solvve.course.dto;

import java.util.UUID;

public class RoleVoteCompliantCreateDTO {

    private UUID userId;

    private UUID roleId;

    private UUID roleVoteId;

    private String description;

    private String moderatedStatus;

    private UUID moderatorId;

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

        RoleVoteCompliantCreateDTO that = (RoleVoteCompliantCreateDTO) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;
        if (roleVoteId != null ? !roleVoteId.equals(that.roleVoteId) : that.roleVoteId != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (moderatedStatus != null ? !moderatedStatus.equals(that.moderatedStatus) : that.moderatedStatus != null)
            return false;
        return moderatorId != null ? moderatorId.equals(that.moderatorId) : that.moderatorId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + (roleVoteId != null ? roleVoteId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (moderatedStatus != null ? moderatedStatus.hashCode() : 0);
        result = 31 * result + (moderatorId != null ? moderatorId.hashCode() : 0);
        return result;
    }
}
