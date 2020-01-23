package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
public class Grants implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_type_id", nullable = false)
    private UserTypes userTypeId;

    @Enumerated(EnumType.STRING)
    private UserPermType userPermission;

    private String objectName;

    @ManyToOne
    @JoinColumn(name = "granted_by", referencedColumnName = "id", nullable = false, updatable = false)
    private PortalUser grantedBy;
}
