package solvve.course.domain;

import javax.persistence.*;
import java.util.*;

@Entity
public class Countries {

    @Id
    @GeneratedValue()
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "movieProdCountries")
    private Set<Movie> movies = new HashSet<>();

    /*@OneToMany(
            mappedBy = "tag",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MovieProdCountries> movieProdCountries = new ArrayList<>();*/

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public List<MovieProdCountries> getMovieProdCountries() {
        return movieProdCountries;
    }

    public void setMovieProdCountries(List<MovieProdCountries> movieProdCountries) {
        this.movieProdCountries = movieProdCountries;
    }*/

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
