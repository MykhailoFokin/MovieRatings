package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class MovieVote extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private PortalUser portalUser;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Movie movie;

    @Enumerated(EnumType.ORDINAL)
    private UserVoteRatingType rating;
}
