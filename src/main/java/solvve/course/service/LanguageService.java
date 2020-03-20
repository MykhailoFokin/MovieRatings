package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Language;
import solvve.course.dto.LanguageCreateDTO;
import solvve.course.dto.LanguagePatchDTO;
import solvve.course.dto.LanguagePutDTO;
import solvve.course.dto.LanguageReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.LanguageRepository;

import java.util.UUID;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private TranslationService translationService;

    @Transactional(readOnly = true)
    public LanguageReadDTO getLanguage(UUID id) {
        Language language = getLanguageRequired(id);
        return translationService.translate(language, LanguageReadDTO.class);
    }

    public LanguageReadDTO createLanguage(LanguageCreateDTO create) {
        Language language = translationService.translate(create, Language.class);

        language = languageRepository.save(language);
        return translationService.translate(language, LanguageReadDTO.class);
    }

    public LanguageReadDTO patchLanguage(UUID id, LanguagePatchDTO patch) {
        Language language = getLanguageRequired(id);

        translationService.map(patch, language);

        language = languageRepository.save(language);
        return translationService.translate(language, LanguageReadDTO.class);
    }

    public void deleteLanguage(UUID id) {
        languageRepository.delete(getLanguageRequired(id));
    }

    public LanguageReadDTO updateLanguage(UUID id, LanguagePutDTO put) {
        Language language = getLanguageRequired(id);

        translationService.updateEntity(put, language);

        language = languageRepository.save(language);
        return translationService.translate(language, LanguageReadDTO.class);
    }

    private Language getLanguageRequired(UUID id) {
        return languageRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(Language.class, id);
        });
    }

}
