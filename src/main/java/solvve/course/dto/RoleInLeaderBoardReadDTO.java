package solvve.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RoleInLeaderBoardReadDTO {

    private UUID id;

    private String roleTitle;

    private String movieTitle;

    private Double averageRating;

    private long votesCount;
}
