package solvve.course.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CountryReadDTO {

    private UUID id;

    private String name;
}
