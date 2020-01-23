package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
public class Movie {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private Short year; // year of production

    private String genres; // type of genres

    private String description; // short movie description

    // Company Credits
    // Same as Crew. Entity Companies and Entity for linkage
    // possible types:
    /*Production Companies
    Distributors
    Special Effects
    Other Companies*/
    private String companies;

    // Technical Specifications
    // it will be dictionaries of appropriate information
    private String soundMix; // sound technologies used in movie
    private String colour; // optional for color scheme
    private String aspectRatio; // examples: 1.43 : 1 (some scenes: 70mm IMAX); 1.90 : 1 (some scenes: Digital IMAX); 2.39 : 1;
    private String camera; // example: Arriflex 235, Panavision Primo Lenses; Arriflex 435, Panavision Primo Lenses; IMAX MSM 9802, Hasselblad Lenses (some scenes)
    private String laboratory; // post production companies, etc

    // Details
    private String languages; // original production language
    private String filmingLocations; // many-to-many. addresses to movies.

    private String critique; // movie 1-8 critique 1-1(1-8) crew

    private Boolean isPublished;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "movie_prod_countries",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "country_id")})
    //private Set<Countries> movieProdCountries = new HashSet<>();
    private Set<Countries> movieProdCountries;

    @OneToMany(mappedBy = "movieId")
    private Set<Crew> crews;

    @OneToMany(mappedBy = "movieId")
    private Set<MovieReview> movieReview;

    @OneToMany(mappedBy = "movieId")
    private Set<MovieReviewCompliant> movieReviewCompliants;

    @OneToMany(mappedBy = "movieId")
    private Set<MovieReviewFeedback> movieReviewFeedbacks;

    @OneToMany(mappedBy = "movieId")
    private Set<MovieVote> movieVotes;

    @OneToMany(mappedBy = "movieId")
    private Set<ReleaseDetails> releaseDetails;

    /*@OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MovieProdCountries> movieProdCountries = new ArrayList<>();

    public void addMovieProdCountries(Countries countries) {
        MovieProdCountries movieProdCountry = new MovieProdCountries(this, countries);
        movieProdCountries.add(movieProdCountry);
        //countries.getMovieProdCountries().add(movieProdCountry);
    }

    public void removeMovieProdCountries(Countries countries) {
        for (Iterator<MovieProdCountries> iterator = movieProdCountries.iterator(); iterator.hasNext(); ) {
            MovieProdCountries movieProdCountry = iterator.next();

            if (movieProdCountry.getMovie().equals(this) &&
                    movieProdCountry.getCountries().equals(countries)) {
                iterator.remove();
                //movieProdCountry.getCountries().getMovieProdCountries().remove(movieProdCountry);
                movieProdCountry.setMovie(null);
                movieProdCountry.setCountries(null);
            }
        }
    }

    public List<MovieProdCountries> getMovieProdCountries() {
        return movieProdCountries;
    }

    public void setMovieProdCountries(List<MovieProdCountries> movieProdCountries) {
        this.movieProdCountries = movieProdCountries;
    }*/
}
