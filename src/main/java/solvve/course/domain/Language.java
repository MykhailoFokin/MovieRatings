package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Entity
public class Language extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @NotNull
    private LanguageType name;

    @ManyToMany(mappedBy = "movieProdLanguages")
    private Set<Movie> movies;

    @ManyToMany(mappedBy = "countryLanguages")
    private Set<Country> countries;
}
