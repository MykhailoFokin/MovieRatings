package solvve.course.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class MasterReadDTO {

    private UUID id;

    private String name;

    private String phone;

    private String about;

    private Instant createdAt;

    private Instant modifiedAt;
}
