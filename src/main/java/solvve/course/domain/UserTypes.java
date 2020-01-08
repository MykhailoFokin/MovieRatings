package solvve.course.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Entity
public class UserTypes implements Serializable {

    @Id
    @GeneratedValue()
    private UUID id;

    @Enumerated(EnumType.STRING)
    private UserGroupType userGroup;

    @OneToMany(mappedBy="userTypeId")
    private Set<Grants> grants;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserGroupType getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroupType userGroup) {
        this.userGroup = userGroup;
    }

    public Set<Grants> getUserTypes() {
        return grants;
    }

    public void setUserTypes(Set<Grants> grants) {
        this.grants = grants;
    }
}
