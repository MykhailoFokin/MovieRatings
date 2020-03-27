package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class CrewTypePatchDTO {

    @Size(min = 1, max = 255)
    private String name;

    private UUID crewId;
}
