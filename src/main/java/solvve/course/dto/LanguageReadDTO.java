package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.LanguageType;

import java.util.UUID;

@Data
public class LanguageReadDTO {

    private UUID Id;

    private LanguageType name;
}
