package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
public class UserTypoRequest extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id", nullable = false)
    private PortalUser requester;

    @ManyToOne
    @JoinColumn(name = "moderator_id", referencedColumnName = "id")
    private PortalUser moderator;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ModeratorTypoReviewStatusType moderatorTypoReviewStatusType;

    private Instant fixAppliedDate;

    @NotNull
    private String sourceText;

    @NotNull
    private String proposedText;

    @Size(min = 1, max = 1000)
    private String approvedText;

    @ManyToOne
    @JoinColumn(name = "news_id", referencedColumnName = "id")
    private News news;

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
