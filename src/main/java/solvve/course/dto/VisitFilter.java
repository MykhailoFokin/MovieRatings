package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.VisitStatus;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class VisitFilter {

    private UUID portalUserId;

    private Set<VisitStatus> statuses;

    private Instant startAtFrom;

    private Instant startAtTo;
}
