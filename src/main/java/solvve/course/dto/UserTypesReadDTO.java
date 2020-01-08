package solvve.course.dto;

import solvve.course.domain.Grants;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserTypes;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public class UserTypesReadDTO implements Serializable {

    private UUID id;

    private UserGroupType userGroup;

    private Set<Grants> userTypes;

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
        return userTypes;
    }

    public void setUserTypes(Set<Grants> userTypes) {
        this.userTypes = userTypes;
    }
}
