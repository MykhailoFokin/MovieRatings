package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieProductionType;

import java.util.UUID;

@Data
public class MovieCompanyPatchDTO {

    private MovieProductionType movieProductionType;

    private String description;

    private UUID companyId;
}
