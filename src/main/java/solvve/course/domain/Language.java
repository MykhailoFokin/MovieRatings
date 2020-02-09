package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
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
