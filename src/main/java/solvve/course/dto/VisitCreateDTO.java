package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Master;
import solvve.course.domain.PortalUser;
import solvve.course.domain.VisitStatus;

import java.time.Instant;
import java.util.UUID;

@Data
public class VisitCreateDTO {

    private UUID userId;

    private UUID masterId;

    private Instant startAt;

    private Instant finishAt;

    private VisitStatus status;
}
