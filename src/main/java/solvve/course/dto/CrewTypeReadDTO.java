package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Crew;

import java.util.UUID;

@Data
public class CrewTypeReadDTO {

    private UUID id;

    private String name;

    private UUID crew;
}
