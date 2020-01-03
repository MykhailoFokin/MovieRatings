package solvve.course.dto;

import solvve.course.domain.Grants;
import solvve.course.domain.UserPermType;
import solvve.course.domain.UserTypes;

import java.util.UUID;

public class GrantsReadDTO {

    private UUID id;

    private UserTypes userTypeId;

    private UserPermType userPermission;

    private String objectName;

    private UUID grantedBy;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserTypes getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(UserTypes userTypeId) {
        this.userTypeId = userTypeId;
    }

    public UserPermType getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(UserPermType userPermission) {
        this.userPermission = userPermission;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public UUID getGrantedBy() {
        return grantedBy;
    }

    public void setGrantedBy(UUID grantedBy) {
        this.grantedBy = grantedBy;
    }
}
