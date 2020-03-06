package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.LanguageType;

import java.time.Instant;
import java.util.UUID;

@Data
public class LanguageReadDTO {

    private UUID id;

    private LanguageType name;

    private Instant createdAt;

    private Instant updatedAt;
}
