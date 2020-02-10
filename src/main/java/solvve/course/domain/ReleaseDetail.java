package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
public class ReleaseDetail {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Movie movieId;

    private LocalDate releaseDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Country countryId;
}
