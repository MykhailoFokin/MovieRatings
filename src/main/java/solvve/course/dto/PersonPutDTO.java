package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Crew;
import solvve.course.domain.Role;

import java.util.Set;

@Data
public class PersonPutDTO {

    private String surname;

    private String name;

    private String middleName;
}
