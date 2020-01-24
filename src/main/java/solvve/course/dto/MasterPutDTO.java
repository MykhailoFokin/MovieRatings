package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Visit;

import java.util.Set;

@Data
public class MasterPutDTO {

    private String name;

    private String phone;

    private String about;

    private Set<Visit> visits;
}
