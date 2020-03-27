package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Entity
public class MovieCompany extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private MovieProductionType movieProductionType;

    @Size(min = 1, max = 1000)
    private String description;

    @ManyToMany(mappedBy = "movieProdCompanies", cascade = CascadeType.REMOVE)
    private Set<Movie> movies;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private CompanyDetails companyDetails;
}
