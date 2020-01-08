package solvve.course.dto;

import java.util.UUID;

public class RoleSpoilerDataReadDTO {

    private UUID id;

    private UUID roleReviewId;

    private int startIndex;

    private int endIndex;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRoleReviewId() {
        return roleReviewId;
    }

    public void setRoleReviewId(UUID roleReviewId) {
        this.roleReviewId = roleReviewId;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
