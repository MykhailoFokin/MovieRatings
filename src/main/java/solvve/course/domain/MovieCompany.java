package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
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
    private CompanyDetails companyDetails;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
