package solvve.course.dto;

import solvve.course.domain.UserVoteRatingType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

public class RoleVoteReadDTO {

    private UUID id;

    private UUID userId;

    private UUID roleId;

    private int rating;

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

    public UserVoteRatingType getRating() { return UserVoteRatingType.parse(this.rating); }

    public void setRating(UserVoteRatingType rating) {
        this.rating = rating.getValue();
    }
}
