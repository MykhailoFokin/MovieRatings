package solvve.course.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CompanyDetailsReadDTO {

    private UUID id;

    private String name;

    private String overview;

    private LocalDate yearOfFoundation;
}
