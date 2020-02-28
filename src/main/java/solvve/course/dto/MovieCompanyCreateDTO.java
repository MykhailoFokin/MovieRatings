package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieProductionType;

import java.util.UUID;

@Data
public class MovieCompanyCreateDTO {

    private MovieProductionType movieProductionType;

    private String description;

    private UUID companyDetailsId;
}
