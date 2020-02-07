package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
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
