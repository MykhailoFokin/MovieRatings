package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class UserType {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private UserGroupType userGroup;

    @OneToMany(mappedBy="userTypeId")
    private Set<UserGrant> userGrants;

    @OneToOne(mappedBy = "userType")
    private PortalUser portalUser;
}
