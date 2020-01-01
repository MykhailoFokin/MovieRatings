package solvve.course.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Countries {

    @Id
    @GeneratedValue()
    private UUID id;

    private String name;

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
}
