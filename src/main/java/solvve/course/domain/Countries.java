package solvve.course.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
public class Countries {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "movieProdCountries")
    private Set<Movie> movies;

    @OneToOne(mappedBy = "countryId")
    private ReleaseDetails releaseDetails;
}
