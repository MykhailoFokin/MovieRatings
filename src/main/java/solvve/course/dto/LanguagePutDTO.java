package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.LanguageType;

@Data
public class LanguagePutDTO {

    private LanguageType name;
}
