package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class ReleaseDetail extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @NotNull
    private Movie movie;

    private LocalDate releaseDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Country country;
}
