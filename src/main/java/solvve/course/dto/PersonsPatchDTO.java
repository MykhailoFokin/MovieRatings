package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Crew;
import solvve.course.domain.Role;

import java.util.Set;

@Data
public class PersonsPatchDTO {

    private String surname;

    private String name;

    private String middleName;

    private Set<Crew> crews;

    private Role role;
}
