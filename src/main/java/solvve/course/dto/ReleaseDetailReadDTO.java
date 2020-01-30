package solvve.course.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ReleaseDetailReadDTO {

    private UUID id;

    private UUID movieId;

    private LocalDate releaseDate;

    private UUID countryId;
}
