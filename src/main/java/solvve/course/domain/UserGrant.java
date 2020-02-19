package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
