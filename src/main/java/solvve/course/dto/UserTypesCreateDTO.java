package solvve.course.dto;

import java.util.UUID;

public class UserTypesCreateDTO {

    private String userGroup;

    private String type;

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTypesCreateDTO that = (UserTypesCreateDTO) o;

        if (userGroup != null ? !userGroup.equals(that.userGroup) : that.userGroup != null) return false;
        return type != null ? type.equals(that.type) : that.type == null;
    }

    @Override
    public int hashCode() {
        int result = userGroup != null ? userGroup.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
