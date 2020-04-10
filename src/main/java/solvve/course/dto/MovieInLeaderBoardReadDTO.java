package solvve.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MovieInLeaderBoardReadDTO {

    private UUID id;

    private String title;

    private Double averageRating;

    private long votesCount;
}
