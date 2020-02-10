package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
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
