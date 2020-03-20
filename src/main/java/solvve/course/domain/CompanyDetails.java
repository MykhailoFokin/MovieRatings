package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class CompanyDetails extends AbstractEntity {

    @OneToMany(mappedBy = "companyDetails", cascade = CascadeType.PERSIST)
    private Set<MovieCompany> movieProdTypeCompanies = new HashSet<MovieCompany>();

    private String name;

    private String overview;

    private LocalDate yearOfFoundation;
}
