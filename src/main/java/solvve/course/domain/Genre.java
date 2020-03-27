package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class Genre extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Movie movie;

    @Enumerated(EnumType.STRING)
    @NotNull
    private MovieGenreType name;
}
