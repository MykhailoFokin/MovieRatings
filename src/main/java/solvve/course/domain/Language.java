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
public class Language {

    @Id
    @GeneratedValue
    private UUID Id;

    @Enumerated(EnumType.STRING)
    private LanguageType name;

    @ManyToMany(mappedBy = "movieProdLanguages")
    private Set<Movie> movies;

    @ManyToMany(mappedBy = "countryLanguages")
    private Set<Country> countries;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
