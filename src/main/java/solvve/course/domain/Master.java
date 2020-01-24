package solvve.course.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class Master {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String phone;

    private String about;

    @OneToMany(mappedBy = "masterId")
    private Set<Visit> visits;
}