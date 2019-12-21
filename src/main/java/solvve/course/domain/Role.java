package solvve.course.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;
import lombok.Data;

@Entity
@Data
public class Role {

    @Id
    @GeneratedValue()
    private UUID id;

    private String title;

    private String roleType; // main, second, etc

    private String description;
}
