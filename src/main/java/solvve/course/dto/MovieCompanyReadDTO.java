package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieProductionType;

import java.time.Instant;
import java.util.UUID;

@Data
public class MovieCompanyReadDTO {

    private UUID id;

    private MovieProductionType movieProductionType;

    private String description;

    private UUID companyId;

    private Instant createdAt;

    private Instant updatedAt;
}
