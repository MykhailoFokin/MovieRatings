package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieProductionType;

import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class MovieCompanyPatchDTO {

    private MovieProductionType movieProductionType;

    @Size(min = 1, max = 1000)
    private String description;

    private UUID companyDetailsId;
}
