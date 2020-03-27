package solvve.course.dto;

import lombok.Data;
import solvve.course.domain.LanguageType;

import javax.validation.constraints.NotNull;

@Data
public class LanguagePutDTO {

    @NotNull
    private LanguageType name;
}
