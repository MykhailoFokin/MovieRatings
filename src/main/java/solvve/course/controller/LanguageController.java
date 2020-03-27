package solvve.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import solvve.course.dto.LanguageCreateDTO;
import solvve.course.dto.LanguagePatchDTO;
import solvve.course.dto.LanguagePutDTO;
import solvve.course.dto.LanguageReadDTO;
import solvve.course.service.LanguageService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/languages")
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @GetMapping("/{id}")
    public LanguageReadDTO getLanguage(@PathVariable UUID id) {
        return languageService.getLanguage(id);
    }

    @PostMapping
    public LanguageReadDTO createLanguage(@RequestBody @Valid LanguageCreateDTO createDTO) {
        return languageService.createLanguage(createDTO);
    }

    @PatchMapping("/{id}")
    public LanguageReadDTO patchLanguage(@PathVariable UUID id, @RequestBody LanguagePatchDTO patch) {
        return languageService.patchLanguage(id, patch);
    }

    @DeleteMapping("/{id}")
    public void deleteLanguage(@PathVariable UUID id) {
        languageService.deleteLanguage(id);
    }

    @PutMapping("/{id}")
    public LanguageReadDTO putLanguage(@PathVariable UUID id, @RequestBody @Valid LanguagePutDTO put) {
        return languageService.updateLanguage(id, put);
    }
}
