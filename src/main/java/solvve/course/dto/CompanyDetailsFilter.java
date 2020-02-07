package solvve.course.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CompanyDetailsFilter {

    private String name;

    private LocalDate yearOfFoundation;

    private List<LocalDate> yearsOfFoundation;
}
