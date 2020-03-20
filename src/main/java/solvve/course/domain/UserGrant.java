package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class UserGrant extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private UserPermType userPermission;

    private String objectName;

    @ManyToOne
    @JoinColumn(name = "granted_by", referencedColumnName = "id", nullable = false, updatable = false)
    private PortalUser grantedBy;
}
