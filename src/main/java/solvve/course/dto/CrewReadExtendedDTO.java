package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CrewReadExtendedDTO {

    private UUID id;

    private PersonReadDTO personId;

    private MovieReadDTO movieId;

    private CrewTypeReadDTO crewType;

    private String description;
}
