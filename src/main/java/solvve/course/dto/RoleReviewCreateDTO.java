package solvve.course.dto;

import java.util.UUID;

public class RoleReviewCreateDTO {

    private UUID userId;

    private UUID roleId;

    private String textReview;

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

    public String getTextReview() {
        return textReview;
    }

    public void setTextReview(String textReview) {
        this.textReview = textReview;
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
        if (!(o instanceof RoleReviewCreateDTO)) return false;

        RoleReviewCreateDTO that = (RoleReviewCreateDTO) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;
        if (textReview != null ? !textReview.equals(that.textReview) : that.textReview != null) return false;
        if (moderatedStatus != that.moderatedStatus) return false;
        return moderatorId != null ? moderatorId.equals(that.moderatorId) : that.moderatorId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + (textReview != null ? textReview.hashCode() : 0);
        result = 31 * result + (moderatedStatus != null ? moderatedStatus.hashCode() : 0);
        result = 31 * result + (moderatorId != null ? moderatorId.hashCode() : 0);
        return result;
    }
}
