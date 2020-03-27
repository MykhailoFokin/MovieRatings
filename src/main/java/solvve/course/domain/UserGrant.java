package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
public class UserGrant extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false)
    @NotNull
    private UserType userType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserPermType userPermission;

    @NotNull
    @Size(min = 1, max = 255)
    private String objectName;

    @ManyToOne
    @JoinColumn(name = "granted_by", referencedColumnName = "id", nullable = false, updatable = false)
    private PortalUser grantedBy;
}
