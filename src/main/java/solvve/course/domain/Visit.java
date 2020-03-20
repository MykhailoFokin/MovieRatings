package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
public class Visit extends AbstractEntity {

    @ManyToOne
    private PortalUser portalUser;

    private Instant startAt;

    private Instant finishAt;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;
}
