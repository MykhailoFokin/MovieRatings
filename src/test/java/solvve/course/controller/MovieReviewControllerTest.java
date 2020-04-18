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
import solvve.course.domain.MovieReview;
import solvve.course.dto.MovieReviewCreateDTO;
import solvve.course.dto.MovieReviewPatchDTO;
import solvve.course.dto.MovieReviewPutDTO;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.MovieReviewService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieReviewController.class)
public class MovieReviewControllerTest extends BaseControllerTest {

    @MockBean
    private MovieReviewService movieReviewService;

    @Test
    public void testGetMovieReview() throws Exception {
        MovieReviewReadDTO movieReview = generateObject(MovieReviewReadDTO.class);

        Mockito.when(movieReviewService.getMovieReview(movieReview.getId())).thenReturn(movieReview);

        String resultJson = mvc.perform(get("/api/v1/moviereviews/{id}", movieReview.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(movieReview);

        Mockito.verify(movieReviewService).getMovieReview(movieReview.getId());
    }

    @Test
    public void testGetMovieReviewWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieReview.class,wrongId);
        Mockito.when(movieReviewService.getMovieReview(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/moviereviews/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/moviereviews/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMovieReview() throws Exception {

        MovieReviewCreateDTO create = generateObject(MovieReviewCreateDTO.class);

        MovieReviewReadDTO read = generateObject(MovieReviewReadDTO.class);

        Mockito.when(movieReviewService.createMovieReview(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewReadDTO actualMovieReview = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assertions.assertThat(actualMovieReview).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReview() throws Exception {

        MovieReviewPatchDTO patchDTO = generateObject(MovieReviewPatchDTO.class);

        MovieReviewReadDTO read = generateObject(MovieReviewReadDTO.class);

        Mockito.when(movieReviewService.patchMovieReview(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/moviereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewReadDTO actualMovieReview = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assert.assertEquals(read, actualMovieReview);
    }

    @Test
    public void testDeleteMovieReview() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/moviereviews/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(movieReviewService).deleteMovieReview(id);
    }

    @Test
    public void testPutMovieReview() throws Exception {

        MovieReviewPutDTO putDTO = generateObject(MovieReviewPutDTO.class);

        MovieReviewReadDTO read = generateObject(MovieReviewReadDTO.class);

        Mockito.when(movieReviewService.updateMovieReview(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewReadDTO actualMovieReview = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assert.assertEquals(read, actualMovieReview);
    }

    @Test
    public void testCreateMovieReviewValidationFailed() throws Exception {
        MovieReviewCreateDTO create = new MovieReviewCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/moviereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewService, Mockito.never()).createMovieReview(ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieReviewValidationFailed() throws Exception {
        MovieReviewPutDTO put = new MovieReviewPutDTO();

        String resultJson = mvc.perform(put("/api/v1/moviereviews/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewService, Mockito.never()).updateMovieReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieReviewCheckLimitBorders() throws Exception {

        MovieReviewPutDTO putDTO = generateObject(MovieReviewPutDTO.class);
        putDTO.setTextReview("T");

        MovieReviewReadDTO read = generateObject(MovieReviewReadDTO.class);

        Mockito.when(movieReviewService.updateMovieReview(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewReadDTO actualMovieReview = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assert.assertEquals(read, actualMovieReview);

        // Check upper border
        putDTO.setTextReview(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(put("/api/v1/moviereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieReview = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assert.assertEquals(read, actualMovieReview);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieReviewPutDTO put = generateObject(MovieReviewPutDTO.class);
        put.setTextReview("");

        String resultJson = mvc.perform(put("/api/v1/moviereviews/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewService, Mockito.never()).updateMovieReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieReviewPutDTO put = generateObject(MovieReviewPutDTO.class);
        put.setTextReview(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(put("/api/v1/moviereviews/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewService, Mockito.never()).updateMovieReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieReviewCreateDTO create = generateObject(MovieReviewCreateDTO.class);
        create.setTextReview("");

        String resultJson = mvc.perform(post("/api/v1/moviereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewService, Mockito.never()).createMovieReview(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieReviewCreateDTO create = generateObject(MovieReviewCreateDTO.class);
        create.setTextReview(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(post("/api/v1/moviereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewService, Mockito.never()).createMovieReview(ArgumentMatchers.any());
    }

    @Test
    public void testCreateMovieReviewCheckStingBorders() throws Exception {

        MovieReviewCreateDTO create = generateObject(MovieReviewCreateDTO.class);
        create.setTextReview("1");

        MovieReviewReadDTO read = generateObject(MovieReviewReadDTO.class);

        Mockito.when(movieReviewService.createMovieReview(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewReadDTO actualMovieReview = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assertions.assertThat(actualMovieReview).isEqualToComparingFieldByField(read);

        create.setTextReview(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(post("/api/v1/moviereviews")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieReview = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assertions.assertThat(actualMovieReview).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewCheckStringBorders() throws Exception {

        MovieReviewPatchDTO patchDTO = generateObject(MovieReviewPatchDTO.class);
        patchDTO.setTextReview("1");

        MovieReviewReadDTO read = generateObject(MovieReviewReadDTO.class);

        Mockito.when(movieReviewService.patchMovieReview(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/moviereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewReadDTO actualMovieReview = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assert.assertEquals(read, actualMovieReview);

        patchDTO.setTextReview(StringUtils.repeat("*", 1000));

        resultJson = mvc.perform(patch("/api/v1/moviereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovieReview = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assert.assertEquals(read, actualMovieReview);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieReviewPatchDTO patch = generateObject(MovieReviewPatchDTO.class);
        patch.setTextReview("");

        String resultJson = mvc.perform(patch("/api/v1/moviereviews/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewService, Mockito.never()).patchMovieReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieReviewPatchDTO patch = generateObject(MovieReviewPatchDTO.class);
        patch.setTextReview(StringUtils.repeat("*", 1001));

        String resultJson = mvc.perform(patch("/api/v1/moviereviews/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewService, Mockito.never()).patchMovieReview(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
