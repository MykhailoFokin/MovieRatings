package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieProductionType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class MovieCompanyCreateDTO {

    private MovieProductionType movieProductionType;

    @Size(min = 1, max = 1000)
    private String description;

    @NotNull
    private UUID companyDetailsId;
}
