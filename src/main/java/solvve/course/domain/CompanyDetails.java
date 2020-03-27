package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class CompanyDetails extends AbstractEntity {

    @OneToMany(mappedBy = "companyDetails", cascade = CascadeType.PERSIST)
    private Set<MovieCompany> movieProdTypeCompanies = new HashSet<MovieCompany>();

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @Size(min = 1, max = 1000)
    private String overview;

    private LocalDate yearOfFoundation;
}
