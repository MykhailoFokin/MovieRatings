package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Visit;

import java.util.Set;
import java.util.UUID;

@Data
public class MasterReadDTO {

    private UUID id;

    private String name;

    private String phone;

    private String about;
}
