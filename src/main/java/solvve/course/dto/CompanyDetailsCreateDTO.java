package solvve.course.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompanyDetailsCreateDTO {

    private String name;

    private String overview;

    private LocalDate yearOfFoundation;
}
