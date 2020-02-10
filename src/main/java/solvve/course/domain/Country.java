package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
public class Country {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "movieProdCountries")
    private Set<Movie> movies;

    @OneToMany(mappedBy = "countryId")
    private Set<ReleaseDetail> releaseDetails;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "country_languages",
            joinColumns = {@JoinColumn(name = "country_id")},
            inverseJoinColumns = {@JoinColumn(name = "language_id")})
    private Set<Language> countryLanguages;
}
