package solvve.course.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ReleaseDetailPutDTO {

    @NotNull
    private UUID movieId;

    private LocalDate releaseDate;

    private UUID countryId;
}
