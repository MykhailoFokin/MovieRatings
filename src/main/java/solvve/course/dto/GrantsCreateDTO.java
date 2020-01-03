package solvve.course.dto;

import solvve.course.domain.UserPermType;
import solvve.course.domain.UserTypes;

import java.io.Serializable;
import java.util.UUID;

public class GrantsCreateDTO implements Serializable {

    private UserTypes userTypeId;

    private UserPermType userPermission;

    private String objectName;

    private UUID grantedBy;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GrantsCreateDTO that = (GrantsCreateDTO) o;

        if (userTypeId != null ? !userTypeId.equals(that.userTypeId) : that.userTypeId != null) return false;
        if (userPermission != that.userPermission) return false;
        if (objectName != null ? !objectName.equals(that.objectName) : that.objectName != null) return false;
        return grantedBy != null ? grantedBy.equals(that.grantedBy) : that.grantedBy == null;
    }

    @Override
    public int hashCode() {
        int result = userTypeId != null ? userTypeId.hashCode() : 0;
        result = 31 * result + (userPermission != null ? userPermission.hashCode() : 0);
        result = 31 * result + (objectName != null ? objectName.hashCode() : 0);
        result = 31 * result + (grantedBy != null ? grantedBy.hashCode() : 0);
        return result;
    }
}
