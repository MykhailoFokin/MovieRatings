package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class MovieSpoilerData extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    @NotNull
    private MovieReview movieReview;

    private Integer startIndex;

    private Integer endIndex;
}
