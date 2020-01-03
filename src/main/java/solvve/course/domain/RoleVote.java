package solvve.course.domain;

import solvve.course.dto.UserModeratedStatusType;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class RoleVote {

    @Id
    @GeneratedValue()
    private UUID id;

    private UUID userId;

    private UUID roleId;

    private int rating;

    private String description;

    private int spoilerStartIndex;

    private int spoilerEndIndex;

    @Enumerated(EnumType.STRING)
    private UserModeratedStatusType moderatedStatus;

    private UUID moderatorId;

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
}
