package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class UserType {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private UserGroupType userGroup;

    @OneToMany(mappedBy="userTypeId")
    private Set<UserGrant> userGrants;

    @OneToOne(mappedBy = "userTypeId")
    private PortalUser portalUser;
}
