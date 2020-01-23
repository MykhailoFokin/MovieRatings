package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.Master;
import solvve.course.domain.PortalUser;
import solvve.course.domain.VisitStatus;

import java.time.Instant;
import java.util.UUID;

@Data
public class VisitReadDTO {

    private UUID id;

    private PortalUser userId;

    private Master masterId;

    private Instant startAt;

    private Instant finishAt;

    private VisitStatus status;
}
