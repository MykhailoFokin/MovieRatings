package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
public class UserGrant {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserType userTypeId;

    @Enumerated(EnumType.STRING)
    private UserPermType userPermission;

    private String objectName;

    @ManyToOne
    @JoinColumn(name = "granted_by", referencedColumnName = "id", nullable = false, updatable = false)
    private PortalUser grantedBy;
}
