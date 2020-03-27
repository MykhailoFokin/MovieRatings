package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Entity
public class News extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private PortalUser publisher;

    private Instant published;

    @NotNull
    @Size(min = 1, max = 255)
    private String topic;

    @NotNull
    @Size(min = 1, max = 1000)
    private String description;

    @OneToMany(mappedBy = "news", cascade = CascadeType.PERSIST)
    private Set<NewsUserReview> newsUserReviews;

    @OneToMany(mappedBy = "news", cascade = CascadeType.PERSIST)
    private Set<NewsUserReviewNote> newsUserReviewNotes;

    @OneToMany(mappedBy = "news", cascade = CascadeType.PERSIST)
    private Set<UserTypoRequest> userTypoRequests;
}
