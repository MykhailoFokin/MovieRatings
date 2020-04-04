package solvve.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solvve.course.domain.Language;
import solvve.course.dto.LanguageCreateDTO;
import solvve.course.dto.LanguagePatchDTO;
import solvve.course.dto.LanguagePutDTO;
import solvve.course.dto.LanguageReadDTO;
import solvve.course.repository.LanguageRepository;

import java.util.UUID;

@Service
public class LanguageService extends AbstractService {

    @Autowired
    private LanguageRepository languageRepository;

    @Transactional(readOnly = true)
    public LanguageReadDTO getLanguage(UUID id) {
        Language language = repositoryHelper.getByIdRequired(Language.class, id);
        return translationService.translate(language, LanguageReadDTO.class);
    }

    public LanguageReadDTO createLanguage(LanguageCreateDTO create) {
        Language language = translationService.translate(create, Language.class);

        language = languageRepository.save(language);
        return translationService.translate(language, LanguageReadDTO.class);
    }

    public LanguageReadDTO patchLanguage(UUID id, LanguagePatchDTO patch) {
        Language language = repositoryHelper.getByIdRequired(Language.class, id);

        translationService.map(patch, language);

        language = languageRepository.save(language);
        return translationService.translate(language, LanguageReadDTO.class);
    }

    public void deleteLanguage(UUID id) {
        languageRepository.delete(repositoryHelper.getByIdRequired(Language.class, id));
    }

    public LanguageReadDTO updateLanguage(UUID id, LanguagePutDTO put) {
        Language language = repositoryHelper.getByIdRequired(Language.class, id);

        translationService.updateEntity(put, language);

        language = languageRepository.save(language);
        return translationService.translate(language, LanguageReadDTO.class);
    }
}
