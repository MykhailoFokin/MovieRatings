package solvve.course.dto;

import java.util.UUID;

public class CrewReadDTO {

    private UUID id;

    private UUID personId;

    private UUID movieId;

    private UUID crewType;

    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public UUID getMovieId() {
        return movieId;
    }

    public void setMovieId(UUID movieId) {
        this.movieId = movieId;
    }

    public UUID getCrewType() {
        return crewType;
    }

    public void setCrewType(UUID crewType) {
        this.crewType = crewType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
