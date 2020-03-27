package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class CompanyDetailsCreateDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 1000)
    private String overview;

    private LocalDate yearOfFoundation;
}
