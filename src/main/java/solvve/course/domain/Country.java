package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Country {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "movieProdCountries")
    private Set<Movie> movies = new HashSet<Movie>();

    @OneToMany(mappedBy = "countryId", cascade = CascadeType. PERSIST)
    private Set<ReleaseDetail> releaseDetails = new HashSet<ReleaseDetail>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "country_languages",
            joinColumns = {@JoinColumn(name = "country_id")},
            inverseJoinColumns = {@JoinColumn(name = "language_id")})
    private Set<Language> countryLanguages = new HashSet<Language>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;
}
