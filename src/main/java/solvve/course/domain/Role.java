package solvve.course.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Role {

    @Id
    @GeneratedValue()
    private UUID id;

    private String title;

    private String roleType; // main, second, etc

    private String description;

    private UUID personId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getPersonId() { return personId; }

    public void setPersonId(UUID personId) { this.personId = personId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != null ? !id.equals(role.id) : role.id != null) return false;
        if (title != null ? !title.equals(role.title) : role.title != null) return false;
        if (roleType != null ? !roleType.equals(role.roleType) : role.roleType != null) return false;
        if (description != null ? !description.equals(role.description) : role.description != null) return false;
        return personId != null ? personId.equals(role.personId) : role.personId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (roleType != null ? roleType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
        return result;
    }
}
