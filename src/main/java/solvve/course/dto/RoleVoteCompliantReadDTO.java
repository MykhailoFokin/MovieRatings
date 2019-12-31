package solvve.course.dto;

import java.util.UUID;

public class RoleVoteCompliantReadDTO {

    private UUID id;

    private UUID userId;

    private UUID roleId;

    private UUID roleVoteId;

    private String description;

    private String moderatedStatus;

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
}
