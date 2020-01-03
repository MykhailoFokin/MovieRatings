package solvve.course.dto;

import solvve.course.domain.UserConfidenceType;

import java.util.UUID;

public class PortalUsersCreateDTO {

    private String login;

    private String surname;

    private String name;

    private String middleName;

    private UUID userType;

    private UserConfidenceType userConfidence;  // user rating according to activity (set by moderator)

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

    public UserConfidenceType getUserConfidence() {
        return userConfidence;
    }

    public void setUserConfidence(UserConfidenceType userConfidence) {
        this.userConfidence = userConfidence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PortalUsersCreateDTO that = (PortalUsersCreateDTO) o;

        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (middleName != null ? !middleName.equals(that.middleName) : that.middleName != null) return false;
        if (userType != null ? !userType.equals(that.userType) : that.userType != null) return false;
        return userConfidence == that.userConfidence;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (userType != null ? userType.hashCode() : 0);
        result = 31 * result + (userConfidence != null ? userConfidence.hashCode() : 0);
        return result;
    }
}
