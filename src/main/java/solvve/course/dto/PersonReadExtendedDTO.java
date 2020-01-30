package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Crew;
import solvve.course.domain.Role;

import java.util.Set;
import java.util.UUID;

@Data
public class PersonReadExtendedDTO {

    private UUID id;

    private String surname;

    private String name;

    private String middleName;

    private Set<Crew> crews;

    private Role role;
}
