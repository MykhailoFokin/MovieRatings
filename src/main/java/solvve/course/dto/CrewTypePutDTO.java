package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Crew;

import java.util.UUID;

@Data
public class CrewTypePutDTO {

    private String name;

    private UUID crew;
}
