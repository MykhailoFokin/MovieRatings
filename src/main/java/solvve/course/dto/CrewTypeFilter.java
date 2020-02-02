package solvve.course.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CrewTypeFilter {

    private String name;

    private UUID crew;

    private List<String> names;

    private List<UUID> crewIds;
}
