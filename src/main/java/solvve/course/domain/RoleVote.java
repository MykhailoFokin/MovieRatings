package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class RoleVote {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private PortalUser userId;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Role roleId;

    @Enumerated(EnumType.ORDINAL)
    private UserVoteRatingType rating;
}
