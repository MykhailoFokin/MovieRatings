package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Visit;

import java.util.Set;

@Data
public class MasterCreateDTO {

    private String name;

    private String phone;

    private String about;
}
