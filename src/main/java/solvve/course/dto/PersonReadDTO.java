package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class PersonReadDTO {

    private UUID id;

    private String surname;

    private String name;

    private String middleName;

    private Instant createdAt;

    private Instant modifiedAt;
}
