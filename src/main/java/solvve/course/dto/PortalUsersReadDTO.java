package solvve.course.dto;

import java.util.UUID;

public class PortalUsersReadDTO {

    private UUID id;

    private String login;

    private String surname;

    private String name;

    private String middleName;

    private UUID userType;

    private int userConfidence;  // user rating according to activity (set by moderator)

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
}
