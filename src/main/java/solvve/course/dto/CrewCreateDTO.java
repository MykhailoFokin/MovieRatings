package solvve.course.dto;

import java.util.UUID;

public class CrewCreateDTO {

    private UUID personId;

    private String crewType;

    private String description;

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public String getCrewType() {
        return crewType;
    }

    public void setCrewType(String crewType) {
        this.crewType = crewType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CrewCreateDTO that = (CrewCreateDTO) o;

        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (crewType != null ? !crewType.equals(that.crewType) : that.crewType != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = personId != null ? personId.hashCode() : 0;
        result = 31 * result + (crewType != null ? crewType.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
