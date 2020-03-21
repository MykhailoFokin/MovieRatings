package solvve.course.event;

import lombok.Data;
import solvve.course.domain.VisitStatus;

import java.util.UUID;

@Data
public class VisitStatusChangedEvent {

    private UUID visitId;

    private VisitStatus newStatus;
}
