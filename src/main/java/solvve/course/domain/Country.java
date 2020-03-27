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
public class Country extends AbstractEntity {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @ManyToMany(mappedBy = "movieProdCountries")
    private Set<Movie> movies = new HashSet<Movie>();

    @OneToMany(mappedBy = "country", cascade = CascadeType.PERSIST)
    private Set<ReleaseDetail> releaseDetails = new HashSet<ReleaseDetail>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "country_languages",
            joinColumns = {@JoinColumn(name = "country_id")},
            inverseJoinColumns = {@JoinColumn(name = "language_id")})
    private Set<Language> countryLanguages = new HashSet<Language>();
}
