package solvve.course.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompanyDetailsPatchDTO {

    private String name;

    private String overview;

    private LocalDate yearOfFoundation;
}