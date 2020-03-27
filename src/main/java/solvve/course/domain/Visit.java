package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@Entity
public class Visit extends AbstractEntity {

    @ManyToOne
    @NotNull
    private PortalUser portalUser;

    @NotNull
    private Instant startAt;

    @NotNull
    private Instant finishAt;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;
}
