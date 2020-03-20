package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Genre extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Movie movie;

    @Enumerated(EnumType.STRING)
    private MovieGenreType name;
}
