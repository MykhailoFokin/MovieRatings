package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class UserTypes {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private UserGroupType userGroup;

    @OneToMany(mappedBy="userTypeId")
    private Set<Grants> grants;

    @OneToOne(mappedBy = "userType")
    private PortalUser portalUser;
}
