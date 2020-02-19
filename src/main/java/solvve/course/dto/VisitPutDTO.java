package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.VisitStatus;

import java.time.Instant;
import java.util.UUID;

@Data
public class VisitPutDTO {

    private UUID userId;

    private Instant startAt;

    private Instant finishAt;

    private VisitStatus status;
}
