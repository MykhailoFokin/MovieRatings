package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.VisitStatus;

import java.time.Instant;
import java.util.UUID;

@Data
public class VisitReadExtendedDTO {

    private UUID id;

    private PortalUserReadDTO userId;

    private Instant startAt;

    private Instant finishAt;

    private VisitStatus status;

    private Instant createdAt;

    private Instant updatedAt;
}
