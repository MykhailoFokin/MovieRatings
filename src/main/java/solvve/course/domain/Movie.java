package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Movie {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private Short year; // year of production

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private Set<Genre> genres = new HashSet<Genre>(); // type of genres

    private String description; // short movie description

    private String soundMix; // sound technologies used in movie

    private String colour; // optional for color scheme

    private String aspectRatio; // examples: 1.43 : 1 (some scenes: 70mm IMAX); 1.90 : 1

    // example: Arriflex 235, Panavision Primo Lenses; Arriflex 435, Panavision Primo Lenses; IMAX MSM 9802
    private String camera;

    private String laboratory; // post production companies, etc

    private String critique; // movie 1-8 critique 1-1(1-8) crew

    private Boolean isPublished;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "movie_prod_countries",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "country_id")})
    private Set<Country> movieProdCountries;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "movie_prod_companies",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "company_id")})
    private Set<MovieCompany> movieProdCompanies;

    @ManyToMany
    @JoinTable(name = "movie_prod_languages",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "language_id")})
    private Set<Language> movieProdLanguages;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private Set<Crew> crews;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private Set<MovieReview> movieReview;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private Set<MovieReviewCompliant> movieReviewCompliants;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private Set<MovieReviewFeedback> movieReviewFeedbacks;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private Set<MovieVote> movieVotes;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private Set<ReleaseDetail> releaseDetails;

    @OneToOne(mappedBy = "movie")
    private Role role;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private Double averageRating;

    @OneToMany(mappedBy = "news", cascade = CascadeType.PERSIST)
    private Set<UserTypoRequest> userTypoRequests;
}
