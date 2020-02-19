package solvve.course.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CrewFilter {

    private UUID personId;

    private UUID movieId;

    private UUID crewTypeId;

    private String description;

    private List<UUID> personIds;

    private List<UUID> movieIds;

    private List<UUID> crewTypesIds;

    private List<String> descriptions;
}
