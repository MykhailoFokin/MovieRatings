package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.VisitStatus;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
public class VisitCreateDTO {

    @NotNull
    private UUID portalUserId;

    @NotNull
    private Instant startAt;

    @NotNull
    private Instant finishAt;
}
