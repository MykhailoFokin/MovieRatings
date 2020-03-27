package solvve.course.controller;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import solvve.course.domain.Language;
import solvve.course.domain.LanguageType;
import solvve.course.dto.LanguageCreateDTO;
import solvve.course.dto.LanguagePatchDTO;
import solvve.course.dto.LanguagePutDTO;
import solvve.course.dto.LanguageReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.LanguageService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LanguageController.class)
public class LanguageControllerTest extends BaseControllerTest {

    @MockBean
    private LanguageService languageService;

    private LanguageReadDTO createLanguageRead() {
        LanguageReadDTO language = new LanguageReadDTO();
        language.setId(UUID.randomUUID());
        language.setName(LanguageType.ARABIAN);
        return language;
    }

    @Test
    public void testGetLanguage() throws Exception {
        LanguageReadDTO language = createLanguageRead();

        Mockito.when(languageService.getLanguage(language.getId())).thenReturn(language);

        String resultJson = mvc.perform(get("/api/v1/languages/{id}", language.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        LanguageReadDTO actualMovie = objectMapper.readValue(resultJson, LanguageReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(language);

        Mockito.verify(languageService).getLanguage(language.getId());
    }

    @Test
    public void testGetLanguageWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Language.class,wrongId);
        Mockito.when(languageService.getLanguage(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/languages/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetLanguageWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/languages/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateLanguage() throws Exception {

        LanguageCreateDTO create = new LanguageCreateDTO();
        create.setName(LanguageType.ARABIAN);

        LanguageReadDTO read = createLanguageRead();

        Mockito.when(languageService.createLanguage(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/languages")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LanguageReadDTO actualLanguage = objectMapper.readValue(resultJson, LanguageReadDTO.class);
        Assertions.assertThat(actualLanguage).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchLanguage() throws Exception {

        LanguagePatchDTO patchDTO = new LanguagePatchDTO();
        patchDTO.setName(LanguageType.ARABIAN);

        LanguageReadDTO read = createLanguageRead();

        Mockito.when(languageService.patchLanguage(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/languages/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LanguageReadDTO actualLanguage = objectMapper.readValue(resultJson, LanguageReadDTO.class);
        Assert.assertEquals(read, actualLanguage);
    }

    @Test
    public void testDeleteLanguage() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/languages/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(languageService).deleteLanguage(id);
    }

    @Test
    public void testPutLanguage() throws Exception {

        LanguagePutDTO putDTO = new LanguagePutDTO();
        putDTO.setName(LanguageType.ARABIAN);

        LanguageReadDTO read = createLanguageRead();

        Mockito.when(languageService.updateLanguage(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/languages/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        LanguageReadDTO actualLanguage = objectMapper.readValue(resultJson, LanguageReadDTO.class);
        Assert.assertEquals(read, actualLanguage);
    }

    @Test
    public void testCreateLanguageValidationFailed() throws Exception {
        LanguageCreateDTO create = new LanguageCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/languages")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(languageService, Mockito.never()).createLanguage(ArgumentMatchers.any());
    }

    @Test
    public void testPutLanguageValidationFailed() throws Exception {
        LanguagePutDTO put = new LanguagePutDTO();

        String resultJson = mvc.perform(put("/api/v1/languages/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(languageService, Mockito.never()).updateLanguage(ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}
