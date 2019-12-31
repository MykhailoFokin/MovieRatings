package solvve.course.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Crew {

    @Id
    @GeneratedValue()
    private UUID id;

    private UUID personId;

    private UUID movieId;

    private String crewType;

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

    public UUID getMovieId() { return movieId; }

    public void setMovieId(UUID movieId) { this.movieId = movieId; }

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
}