package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class MovieCompany {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private MovieProductionType movieProductionType;

    private String description;

    @ManyToMany(mappedBy = "movieProdCompanies", cascade = CascadeType.REMOVE)
    private Set<Movie> movies;

    @ManyToOne
    @JoinColumn(nullable = false)
    private CompanyDetails companyId;
}
