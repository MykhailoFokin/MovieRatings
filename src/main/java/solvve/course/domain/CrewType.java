package solvve.course.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Data
@Entity
public class CrewType {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @OneToOne(mappedBy = "crewType")
    private Crew crew;
}
