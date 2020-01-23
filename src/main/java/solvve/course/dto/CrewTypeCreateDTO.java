package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Crew;

@Data
public class CrewTypeCreateDTO {

    private String name;

    private Crew crew;
}
