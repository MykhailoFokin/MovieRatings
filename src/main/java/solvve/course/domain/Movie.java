package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Getter
@Setter
@Entity
public class Movie extends AbstractEntity {

    @NotNull
    @Size(min = 1, max = 255)
    private String title;

    @NotNull
    private Short year; // year of production

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private Set<Genre> genres = new HashSet<Genre>(); // type of genres

    @Size(min = 1, max = 1000)
    private String description; // short movie description

    @Size(min = 1, max = 255)
    private String soundMix; // sound technologies used in movie

    @Size(min = 1, max = 255)
    private String colour; // optional for color scheme

    @Size(min = 1, max = 255)
    private String aspectRatio; // examples: 1.43 : 1 (some scenes: 70mm IMAX); 1.90 : 1

    // example: Arriflex 235, Panavision Primo Lenses; Arriflex 435, Panavision Primo Lenses; IMAX MSM 9802
    @Size(min = 1, max = 255)
    private String camera;

    @Size(min = 1, max = 255)
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

    private Double averageRating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.PERSIST)
    private Set<UserTypoRequest> userTypoRequests;

    private Boolean adult;

    private String originalLanguage;

    private String originalTitle;

    private Long budget;

    private String homepage;

    private String imdbId;

    private Long revenue;

    private Integer runtime;

    private String tagline;
}
