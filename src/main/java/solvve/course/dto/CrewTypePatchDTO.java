package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Crew;

import java.util.UUID;

@Data
public class CrewTypePatchDTO {

    private String name;

    private UUID crew;
}
