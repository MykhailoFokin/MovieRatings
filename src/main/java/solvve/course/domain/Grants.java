package solvve.course.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Grants {

    @Id
    @GeneratedValue()
    private UUID id;

    private UUID userTypeId;

    private String userPermission;

    private String objectName;

    private UUID grantedBy;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(UUID userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(String userPermission) {
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
