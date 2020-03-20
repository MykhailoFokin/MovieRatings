package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    private String topic;

    private String description;

    @OneToMany(mappedBy = "news", cascade = CascadeType.PERSIST)
    private Set<NewsUserReview> newsUserReviews;

    @OneToMany(mappedBy = "news", cascade = CascadeType.PERSIST)
    private Set<NewsUserReviewNote> newsUserReviewNotes;

    @OneToMany(mappedBy = "news", cascade = CascadeType.PERSIST)
    private Set<UserTypoRequest> userTypoRequests;
}
