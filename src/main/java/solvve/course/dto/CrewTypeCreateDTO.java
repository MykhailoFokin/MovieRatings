package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CrewTypeCreateDTO {

    private String name;

    private UUID crewId;
}
