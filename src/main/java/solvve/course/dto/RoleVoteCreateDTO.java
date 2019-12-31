package solvve.course.dto;

import java.util.UUID;

public class RoleVoteCreateDTO {

    private UUID userId;

    private UUID roleId;

    private int rating;

    private String description;

    private int spoilerStartIndex;

    private int spoilerEndIndex;

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSpoilerStartIndex() {
        return spoilerStartIndex;
    }

    public void setSpoilerStartIndex(int spoilerStartIndex) {
        this.spoilerStartIndex = spoilerStartIndex;
    }

    public int getSpoilerEndIndex() {
        return spoilerEndIndex;
    }

    public void setSpoilerEndIndex(int spoilerEndIndex) {
        this.spoilerEndIndex = spoilerEndIndex;
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

        RoleVoteCreateDTO that = (RoleVoteCreateDTO) o;

        if (rating != that.rating) return false;
        if (spoilerStartIndex != that.spoilerStartIndex) return false;
        if (spoilerEndIndex != that.spoilerEndIndex) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (moderatedStatus != null ? !moderatedStatus.equals(that.moderatedStatus) : that.moderatedStatus != null)
            return false;
        return moderatorId != null ? moderatorId.equals(that.moderatorId) : that.moderatorId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + rating;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + spoilerStartIndex;
        result = 31 * result + spoilerEndIndex;
        result = 31 * result + (moderatedStatus != null ? moderatedStatus.hashCode() : 0);
        result = 31 * result + (moderatorId != null ? moderatorId.hashCode() : 0);
        return result;
    }
}