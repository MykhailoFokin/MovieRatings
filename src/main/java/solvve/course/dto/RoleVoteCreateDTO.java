package solvve.course.dto;

import solvve.course.domain.UserVoteRatingType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

public class RoleVoteCreateDTO {

    private UUID userId;

    private UUID roleId;

    private int rating;

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

    public UserVoteRatingType getRating() { return UserVoteRatingType.parse(this.rating); }

    public void setRating(UserVoteRatingType rating) {
        this.rating = rating.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleVoteCreateDTO)) return false;

        RoleVoteCreateDTO that = (RoleVoteCreateDTO) o;

        if (rating != that.rating) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return roleId != null ? roleId.equals(that.roleId) : that.roleId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        result = 31 * result + rating;
        return result;
    }
}
