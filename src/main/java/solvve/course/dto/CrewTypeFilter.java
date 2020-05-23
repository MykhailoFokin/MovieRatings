package solvve.course.dto;

import lombok.Data;

import java.util.List;

@Data
public class CrewTypeFilter {

    private String name;

    private List<String> names;
}
