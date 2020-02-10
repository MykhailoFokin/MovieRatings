package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class CompanyDetails {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(mappedBy = "companyId")
    private Set<MovieCompany> movieProdTypeCompanies = new HashSet<MovieCompany>();

    private String name;

    private String overview;

    private LocalDate yearOfFoundation;
}
