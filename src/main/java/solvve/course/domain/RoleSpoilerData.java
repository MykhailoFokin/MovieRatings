package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class RoleSpoilerData {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private RoleReview roleReviewId;

    private Integer startIndex;

    private Integer endIndex;
}
