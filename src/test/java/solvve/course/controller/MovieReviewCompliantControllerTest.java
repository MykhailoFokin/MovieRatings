package solvve.course.controller;

import liquibase.util.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import solvve.course.domain.MovieReviewCompliant;
import solvve.course.dto.MovieReviewCompliantCreateDTO;
import solvve.course.dto.MovieReviewCompliantPatchDTO;
import solvve.course.dto.MovieReviewCompliantPutDTO;
import solvve.course.dto.MovieReviewCompliantReadDTO;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.MovieReviewCompliantService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieReviewCompliantController.class)
public class MovieReviewCompliantControllerTest extends BaseControllerTest {

    @MockBean
    private MovieReviewCompliantService movieReviewCompliantService;

    @Test
    public void testGetMovieReviewCompliant() throws Exception {
        MovieReviewCompliantReadDTO movieReviewCompliant = generateObject(MovieReviewCompliantReadDTO.class);

        Mockito.when(movieReviewCompliantService.getMovieReviewCompliant(movieReviewCompliant.getId()))
                .thenReturn(movieReviewCompliant);

        String resultJson = mvc.perform(get("/api/v1/moviereviewcompliants/{id}",
                movieReviewCompliant.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        MovieReviewCompliantReadDTO actualMovie = objectMapper
                .readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(movieReviewCompliant);

        Mockito.verify(movieReviewCompliantService).getMovieReviewCompliant(movieReviewCompliant.getId());
    }

    @Test
    public void testGetMovieReviewCompliantWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieReviewCompliant.class,wrongId);
        Mockito.when(movieReviewCompliantService.getMovieReviewCompliant(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/moviereviewcompliants/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewCompliantWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/moviereviewcompliants/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMovieReviewCompliant() throws Exception {

        MovieReviewCompliantCreateDTO create = generateObject(MovieReviewCompliantCreateDTO.class);

        MovieReviewCompliantReadDTO read = generateObject(MovieReviewCompliantReadDTO.class);

        Mockito.when(movieReviewCompliantService.createMovieReviewCompliant(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant = objectMapper
                .readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovieReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewCompliant() throws Exception {

        MovieReviewCompliantPatchDTO patchDTO = generateObject(MovieReviewCompliantPatchDTO.class);

        MovieReviewCompliantReadDTO read = generateObject(MovieReviewCompliantReadDTO.class);

        Mockito.when(movieReviewCompliantService.patchMovieReviewCompliant(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/moviereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant = objectMapper
                .readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);
    }

    @Test
    public void testDeleteMovieReviewCompliant() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/moviereviewcompliants/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(movieReviewCompliantService).deleteMovieReviewCompliant(id);
    }

    @Test
    public void testPutMovieReviewCompliant() throws Exception {

        MovieReviewCompliantPutDTO putDTO = generateObject(MovieReviewCompliantPutDTO.class);

        MovieReviewCompliantReadDTO read = generateObject(MovieReviewCompliantReadDTO.class);

        Mockito.when(movieReviewCompliantService.updateMovieReviewCompliant(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant = objectMapper
                .readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);
    }

    @Test
    public void testCreateMovieReviewCompliantValidationFailed() throws Exception {
        MovieReviewCompliantCreateDTO create = new MovieReviewCompliantCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/moviereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService, Mockito.never()).createMovieReviewCompliant(ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieReviewCompliantValidationFailed() throws Exception {
        MovieReviewCompliantPutDTO put = new MovieReviewCompliantPutDTO();

        String resultJson = mvc.perform(put("/api/v1/moviereviewcompliants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService, Mockito.never()).updateMovieReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieReviewCompliantCheckLimitBorders() throws Exception {

        MovieReviewCompliantPutDTO putDTO = generateObject(MovieReviewCompliantPutDTO.class);
        putDTO.setDescription("J");

        MovieReviewCompliantReadDTO read = generateObject(MovieReviewCompliantReadDTO.class);

        Mockito.when(movieReviewCompliantService.updateMovieReviewCompliant(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant =
                objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);

        // Check upper border
        putDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(put("/api/v1/moviereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieReviewCompliant = objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieReviewCompliantPutDTO put = generateObject(MovieReviewCompliantPutDTO.class);
        put.setDescription("");

        String resultJson = mvc.perform(put("/api/v1/moviereviewcompliants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService, Mockito.never()).updateMovieReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieReviewCompliantPutDTO put = generateObject(MovieReviewCompliantPutDTO.class);
        put.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(put("/api/v1/moviereviewcompliants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService, Mockito.never()).updateMovieReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieReviewCompliantCreateDTO create = generateObject(MovieReviewCompliantCreateDTO.class);
        create.setDescription("");

        String resultJson = mvc.perform(post("/api/v1/moviereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService, Mockito.never()).createMovieReviewCompliant(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieReviewCompliantCreateDTO create = generateObject(MovieReviewCompliantCreateDTO.class);
        create.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(post("/api/v1/moviereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService, Mockito.never()).createMovieReviewCompliant(ArgumentMatchers.any());
    }

    @Test
    public void testCreateMovieReviewCompliantCheckStingBorders() throws Exception {

        MovieReviewCompliantCreateDTO create = generateObject(MovieReviewCompliantCreateDTO.class);
        create.setDescription("D");

        MovieReviewCompliantReadDTO read = generateObject(MovieReviewCompliantReadDTO.class);

        Mockito.when(movieReviewCompliantService.createMovieReviewCompliant(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant =
                objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovieReviewCompliant).isEqualToComparingFieldByField(read);

        create.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post("/api/v1/moviereviewcompliants")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieReviewCompliant = objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assertions.assertThat(actualMovieReviewCompliant).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewCompliantCheckStringBorders() throws Exception {

        MovieReviewCompliantPatchDTO patchDTO = generateObject(MovieReviewCompliantPatchDTO.class);
        patchDTO.setDescription("D");

        MovieReviewCompliantReadDTO read = generateObject(MovieReviewCompliantReadDTO.class);

        Mockito.when(movieReviewCompliantService.patchMovieReviewCompliant(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/moviereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewCompliantReadDTO actualMovieReviewCompliant =
                objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);

        patchDTO.setDescription(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch("/api/v1/moviereviewcompliants/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieReviewCompliant = objectMapper.readValue(resultJson, MovieReviewCompliantReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewCompliant);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieReviewCompliantPatchDTO patch = generateObject(MovieReviewCompliantPatchDTO.class);
        patch.setDescription("");

        String resultJson = mvc.perform(patch("/api/v1/moviereviewcompliants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService, Mockito.never()).patchMovieReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieReviewCompliantPatchDTO patch = generateObject(MovieReviewCompliantPatchDTO.class);
        patch.setDescription(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(patch("/api/v1/moviereviewcompliants/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewCompliantService, Mockito.never()).patchMovieReviewCompliant(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
