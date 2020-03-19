package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class News {

    @Id
    @GeneratedValue
    private UUID id;

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

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
