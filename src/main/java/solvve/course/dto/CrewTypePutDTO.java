package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CrewTypePutDTO {

    private String name;

    private UUID crew;
}
