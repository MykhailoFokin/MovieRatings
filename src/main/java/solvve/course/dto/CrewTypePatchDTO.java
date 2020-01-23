package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Crew;

@Data
public class CrewTypePatchDTO {

    private String name;

    private Crew crew;
}
