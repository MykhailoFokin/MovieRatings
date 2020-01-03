package solvve.course.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Grants implements Serializable {

    @Id
    @GeneratedValue()
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserTypes userTypeId;

    @Enumerated(EnumType.STRING)
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
