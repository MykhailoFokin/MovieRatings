package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Language {

    @Id
    @GeneratedValue
    private UUID Id;

    @Enumerated(EnumType.STRING)
    private LanguageType name;

    @ManyToMany(mappedBy = "movieProdLanguages")
    private Set<Movie> movies;

    @ManyToMany(mappedBy = "countryLanguages")
    private Set<Country> countries;
}
