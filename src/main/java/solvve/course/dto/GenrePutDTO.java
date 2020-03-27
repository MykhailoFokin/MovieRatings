package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.MovieGenreType;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class GenrePutDTO {

    @NotNull
    private UUID movieId;

    @NotNull
    private MovieGenreType name;
}
