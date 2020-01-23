package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
public class Visit {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private PortalUser userId;

    @ManyToOne
    private Master masterId;

    private Instant startAt;

    private Instant finishAt;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;
}
