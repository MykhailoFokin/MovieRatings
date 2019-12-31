package solvve.course.dto;

import java.util.UUID;

public class PortalUsersCreateDTO {

    private String login;

    private String surname;

    private String name;

    private String middleName;

    private UUID userType;

    private int userConfidence;  // user rating according to activity (set by moderator)

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public UUID getUserType() { return userType; }

    public void setUserType(UUID userType) { this.userType = userType; }

    public int getUserConfidence() {
        return userConfidence;
    }

    public void setUserConfidence(int userConfidence) {
        this.userConfidence = userConfidence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PortalUsersCreateDTO that = (PortalUsersCreateDTO) o;

        if (userConfidence != that.userConfidence) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (middleName != null ? !middleName.equals(that.middleName) : that.middleName != null) return false;
        return userType != null ? userType.equals(that.userType) : that.userType == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (userType != null ? userType.hashCode() : 0);
        result = 31 * result + userConfidence;
        return result;
    }
}
