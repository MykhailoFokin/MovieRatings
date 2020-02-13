package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;
}
