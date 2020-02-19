package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CompanyDetails {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToMany(mappedBy = "companyId", cascade = CascadeType.PERSIST)
    private Set<MovieCompany> movieProdTypeCompanies = new HashSet<MovieCompany>();

    private String name;

    private String overview;

    private LocalDate yearOfFoundation;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
