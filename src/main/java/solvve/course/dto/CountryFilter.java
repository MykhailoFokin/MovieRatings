package solvve.course.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CountryFilter {

    private Set<String> names;
}
