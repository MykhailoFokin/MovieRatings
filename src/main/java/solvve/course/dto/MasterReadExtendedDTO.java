package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
public class MasterReadExtendedDTO {

    private UUID id;

    private String name;

    private String phone;

    private String about;

    private Set<VisitReadDTO> visits;

    private Instant createdAt;

    private Instant modifiedAt;
}
