package solvve.course.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

public class RoleReviewCompliantCreateDTO {

    private UUID userId;

    private UUID roleId;

    private UUID roleReviewId;

    private String description;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

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

    public UUID getRoleReviewId() {
        return roleReviewId;
    }

    public void setRoleReviewId(UUID roleReviewId) {
        this.roleReviewId = roleReviewId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserModeratedStatusType getModeratedStatus() {
        return moderatedStatus;
    }

    public void setModeratedStatus(UserModeratedStatusType moderatedStatus) {
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
        if (!(o instanceof RoleReviewCompliantCreateDTO)) return false;

        RoleReviewCompliantCreateDTO that = (RoleReviewCompliantCreateDTO) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;
        if (roleReviewId != null ? !roleReviewId.equals(that.roleReviewId) : that.roleReviewId != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (moderatedStatus != that.moderatedStatus) return false;
        return moderatorId != null ? moderatorId.equals(that.moderatorId) : that.moderatorId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + (roleReviewId != null ? roleReviewId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (moderatedStatus != null ? moderatedStatus.hashCode() : 0);
        result = 31 * result + (moderatorId != null ? moderatorId.hashCode() : 0);
        return result;
    }
}
