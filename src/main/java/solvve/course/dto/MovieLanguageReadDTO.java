package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.LanguageType;

import java.util.UUID;

@Data
public class MovieLanguageReadDTO {

    private UUID id;

    private LanguageType name;
}
