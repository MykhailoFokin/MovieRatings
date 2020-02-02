package solvve.course.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class CountryFilter {

    private Set<String> names;
}
