package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class RoleSpoilerData extends AbstractEntity {

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private RoleReview roleReview;

    private Integer startIndex;

    private Integer endIndex;
}
