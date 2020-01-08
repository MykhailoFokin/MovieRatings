package solvve.course.dto;

import solvve.course.domain.Grants;
import solvve.course.domain.UserGroupType;
import solvve.course.domain.UserTypes;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public class UserTypesCreateDTO implements Serializable {

    private UserGroupType userGroup;

    private Set<Grants> userTypes;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTypesCreateDTO)) return false;

        UserTypesCreateDTO that = (UserTypesCreateDTO) o;

        if (userGroup != that.userGroup) return false;
        return userTypes != null ? userTypes.equals(that.userTypes) : that.userTypes == null;
    }

    @Override
    public int hashCode() {
        int result = userGroup != null ? userGroup.hashCode() : 0;
        result = 31 * result + (userTypes != null ? userTypes.hashCode() : 0);
        return result;
    }
}
