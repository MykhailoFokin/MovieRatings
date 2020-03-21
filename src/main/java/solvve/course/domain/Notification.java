package solvve.course.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Notification extends AbstractEntity {

    private String message;

    private String sourceEntity;

    private String entityIdentifier;
}
