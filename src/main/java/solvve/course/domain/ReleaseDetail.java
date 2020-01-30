package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
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
