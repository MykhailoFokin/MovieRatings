package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.VisitStatus;

import java.time.Instant;
import java.util.UUID;

@Data
public class VisitReadDTO {

    private UUID id;

    private UUID userId;

    private UUID masterId;

    private Instant startAt;

    private Instant finishAt;

    private VisitStatus status;

    private Instant createdAt;

    private Instant modifiedAt;
}
