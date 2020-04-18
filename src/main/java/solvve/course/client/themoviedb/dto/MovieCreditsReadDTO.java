package solvve.course.client.themoviedb.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieCreditsReadDTO {

    private String id;

    private List<MovieCreditsCastReadDTO> cast;

    private List<MovieCreditsCrewReadDTO> crew;
}
