package solvve.course.domain;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class RoleVote {

    @Id
    @GeneratedValue()
    private UUID id;

    private UUID userId;

    private UUID roleId;

    @Enumerated(EnumType.ORDINAL)
    private UserVoteRatingType rating;

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

    public UserVoteRatingType getRating() {
        return rating;
    }

    public void setRating(UserVoteRatingType rating) {
        this.rating = rating;
    }
}
