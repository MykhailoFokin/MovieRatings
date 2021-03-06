package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieProductionType;

import java.util.List;
import java.util.UUID;

@Data
public class MovieCompanyFilter {

    private List<MovieProductionType> movieProductionTypes;

    private UUID companyDetailsId;
}
