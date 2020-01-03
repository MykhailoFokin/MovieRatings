package solvve.course.dto;

import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserTypes;

import java.util.Set;
import java.util.UUID;

public class UserTypesReadDTO {

    private UUID id;

    private UserGroupType userGroup;

    private Set<UserTypes> userTypes;

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

    public Set<UserTypes> getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(Set<UserTypes> userTypes) {
        this.userTypes = userTypes;
    }
}
