package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Data
@Entity
public class ReleaseDetails {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Movie movieId;

    private Date releaseDate;

    @OneToOne
    private Countries countryId;
}
