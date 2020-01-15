package solvve.course.dto;

import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
public class ReleaseDetailsPatchDTO {

    private UUID movieId;

    private Date releaseDate;

    private UUID countryId;
}
