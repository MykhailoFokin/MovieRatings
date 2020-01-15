package solvve.course.dto;

import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
public class ReleaseDetailsReadDTO {

    private UUID id;

    private UUID movieId;

    private Date releaseDate;

    private UUID countryId;
}
