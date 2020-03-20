package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class UserType extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private UserGroupType userGroup;

    @OneToMany(mappedBy = "userType", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<UserGrant> userGrants;

    @OneToOne(mappedBy = "userType", cascade = CascadeType.REMOVE)
    private PortalUser portalUser;
}
