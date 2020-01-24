package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Crew;

@Data
public class CrewTypePutDTO {

    private String name;

    private Crew crew;
}
