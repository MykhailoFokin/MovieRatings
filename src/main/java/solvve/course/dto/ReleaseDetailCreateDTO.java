package solvve.course.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ReleaseDetailCreateDTO {

    private UUID movieId;

    private LocalDate releaseDate;

    private UUID countryId;
}
