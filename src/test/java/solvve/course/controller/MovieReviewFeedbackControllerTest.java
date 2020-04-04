package solvve.course.controller;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import solvve.course.domain.MovieReviewFeedback;
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackPatchDTO;
import solvve.course.dto.MovieReviewFeedbackPutDTO;
import solvve.course.dto.MovieReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.MovieReviewFeedbackService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieReviewFeedbackController.class)
public class MovieReviewFeedbackControllerTest extends BaseControllerTest {

    @MockBean
    private MovieReviewFeedbackService movieReviewFeedbackService;

    @Test
    public void testGetMovieReviewFeedback() throws Exception {
        MovieReviewFeedbackReadDTO movieReviewFeedback = generateObject(MovieReviewFeedbackReadDTO.class);

        Mockito.when(movieReviewFeedbackService.getMovieReviewFeedback(movieReviewFeedback.getId()))
                .thenReturn(movieReviewFeedback);

        String resultJson = mvc.perform(get("/api/v1/moviereviewfeedbacks/{id}",
                movieReviewFeedback.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        MovieReviewFeedbackReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReviewFeedbackReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(movieReviewFeedback);

        Mockito.verify(movieReviewFeedbackService).getMovieReviewFeedback(movieReviewFeedback.getId());
    }

    @Test
    public void testGetMovieReviewFeedbackWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieReviewFeedback.class,wrongId);
        Mockito.when(movieReviewFeedbackService.getMovieReviewFeedback(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/moviereviewfeedbacks/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewFeedbackWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/moviereviewfeedbacks/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMovieReviewFeedback() throws Exception {

        MovieReviewFeedbackCreateDTO create = generateObject(MovieReviewFeedbackCreateDTO.class);

        MovieReviewFeedbackReadDTO read = generateObject(MovieReviewFeedbackReadDTO.class);

        Mockito.when(movieReviewFeedbackService.createMovieReviewFeedback(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviereviewfeedbacks")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewFeedbackReadDTO actualMovieReviewFeedback = objectMapper
                .readValue(resultJson, MovieReviewFeedbackReadDTO.class);
        Assertions.assertThat(actualMovieReviewFeedback).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewFeedback() throws Exception {

        MovieReviewFeedbackPatchDTO patchDTO = generateObject(MovieReviewFeedbackPatchDTO.class);

        MovieReviewFeedbackReadDTO read = generateObject(MovieReviewFeedbackReadDTO.class);

        Mockito.when(movieReviewFeedbackService.patchMovieReviewFeedback(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/moviereviewfeedbacks/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewFeedbackReadDTO actualMovieReviewFeedback = objectMapper
                .readValue(resultJson, MovieReviewFeedbackReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewFeedback);
    }

    @Test
    public void testDeleteMovieReviewFeedback() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/moviereviewfeedbacks/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(movieReviewFeedbackService).deleteMovieReviewFeedback(id);
    }

    @Test
    public void testPutMovieReviewFeedback() throws Exception {

        MovieReviewFeedbackPutDTO putDTO = generateObject(MovieReviewFeedbackPutDTO.class);

        MovieReviewFeedbackReadDTO read = generateObject(MovieReviewFeedbackReadDTO.class);

        Mockito.when(movieReviewFeedbackService.updateMovieReviewFeedback(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviereviewfeedbacks/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewFeedbackReadDTO actualMovieReviewFeedback = objectMapper
                .readValue(resultJson, MovieReviewFeedbackReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewFeedback);
    }

    @Test
    public void testCreateMovieReviewFeedbackValidationFailed() throws Exception {
        MovieReviewFeedbackCreateDTO create = new MovieReviewFeedbackCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/moviereviewfeedbacks")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewFeedbackService, Mockito.never()).createMovieReviewFeedback(ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieReviewFeedbackValidationFailed() throws Exception {
        MovieReviewFeedbackPutDTO put = new MovieReviewFeedbackPutDTO();

        String resultJson = mvc.perform(put("/api/v1/moviereviewfeedbacks/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewFeedbackService, Mockito.never()).updateMovieReviewFeedback(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
