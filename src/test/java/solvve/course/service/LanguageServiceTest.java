package solvve.course.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import solvve.course.BaseTest;
import solvve.course.domain.Language;
import solvve.course.dto.LanguageCreateDTO;
import solvve.course.dto.LanguagePatchDTO;
import solvve.course.dto.LanguagePutDTO;
import solvve.course.dto.LanguageReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.repository.LanguageRepository;
import solvve.course.utils.TestObjectsFactory;

import java.util.UUID;

public class LanguageServiceTest extends BaseTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageService languageService;

    @Test
    public void testGetLanguage() {
        Language language = testObjectsFactory.createLanguage();

        LanguageReadDTO readDTO = languageService.getLanguage(language.getId());
        Assertions.assertThat(readDTO).isEqualToComparingFieldByField(language);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetLanguageWrongId() {
        languageService.getLanguage(UUID.randomUUID());
    }

    @Test
    public void testCreateLanguage() {
        LanguageCreateDTO create = testObjectsFactory.createLanguageCreateDTO();
        LanguageReadDTO read = languageService.createLanguage(create);
        Assertions.assertThat(create).isEqualToComparingFieldByField(read);

        Language language = languageRepository.findById(read.getId()).get();
        Assertions.assertThat(read).isEqualToComparingFieldByField(language);
    }

    @Test
    public void testPatchLanguage() {
        Language language = testObjectsFactory.createLanguage();

        LanguagePatchDTO patch = testObjectsFactory.createLanguagePatchDTO();
        LanguageReadDTO read = languageService.patchLanguage(language.getId(), patch);

        Assertions.assertThat(patch).isEqualToComparingFieldByField(read);

        language = languageRepository.findById(read.getId()).get();
        Assertions.assertThat(language).isEqualToIgnoringGivenFields(read,
                "movies", "countries");
    }

    @Test
    public void testPatchLanguageEmptyPatch() {
        Language language = testObjectsFactory.createLanguage();

        LanguagePatchDTO patch = new LanguagePatchDTO();
        LanguageReadDTO read = languageService.patchLanguage(language.getId(), patch);

        Assert.assertNotNull(read.getName());

        Language languageAfterUpdate = languageRepository.findById(read.getId()).get();

        Assert.assertNotNull(languageAfterUpdate.getName());

        Assertions.assertThat(language).isEqualToIgnoringGivenFields(languageAfterUpdate,
                "movies", "countries");
    }

    @Test
    public void testDeleteLanguage() {
        Language language = testObjectsFactory.createLanguage();

        languageService.deleteLanguage(language.getId());
        Assert.assertFalse(languageRepository.existsById(language.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteLanguageNotFound() {
        languageService.deleteLanguage(UUID.randomUUID());
    }

    @Test
    public void testPutLanguage() {
        Language language = testObjectsFactory.createLanguage();

        LanguagePutDTO put = testObjectsFactory.createLanguagePutDTO();
        LanguageReadDTO read = languageService.updateLanguage(language.getId(), put);

        Assertions.assertThat(put).isEqualToComparingFieldByField(read);

        language = languageRepository.findById(read.getId()).get();
        Assertions.assertThat(language).isEqualToIgnoringGivenFields(read,
                "movies", "countries");
    }

    @Test
    public void testPutLanguageEmptyPut() {
        Language language = testObjectsFactory.createLanguage();

        LanguagePutDTO put = new LanguagePutDTO();
        LanguageReadDTO read = languageService.updateLanguage(language.getId(), put);

        Assert.assertNotNull(read.getName());

        Language languageAfterUpdate = languageRepository.findById(read.getId()).get();

        Assert.assertNotNull(languageAfterUpdate.getName());

        Assertions.assertThat(read).isEqualToIgnoringGivenFields(languageAfterUpdate,
                "movies", "countries");
    }
}
