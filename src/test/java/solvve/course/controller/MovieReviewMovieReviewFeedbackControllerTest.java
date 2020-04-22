package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import solvve.course.domain.MovieReviewFeedback;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.MovieReviewMovieReviewFeedbackService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(controllers = MovieReviewMovieReviewFeedbackController.class)
public class MovieReviewMovieReviewFeedbackControllerTest extends BaseControllerTest {

    @MockBean
    private MovieReviewMovieReviewFeedbackService movieReviewFeedbackService;

    @Test
    public void testGetMovieReviewMovieReviewFeedback() throws Exception {
        MovieReviewReadDTO movieReview = generateObject(MovieReviewReadDTO.class);
        List<MovieReviewFeedbackReadDTO> movieReviewFeedbackReadDTO =
                List.of(createMovieReviewFeedbackRead(movieReview.getId()));

        Mockito.when(movieReviewFeedbackService.getMovieReviewMovieReviewFeedback(movieReview.getId()))
                .thenReturn(movieReviewFeedbackReadDTO);

        String resultJson = mvc.perform(get("/api/v1/movie-reviews/{movieReviewId}/movie-review-feedbacks",
                movieReview.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieReviewFeedbackReadDTO> actualMovie = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieReviewFeedbackReadDTO>>(){});
        Assertions.assertThat(actualMovie).isEqualTo(movieReviewFeedbackReadDTO);

        Mockito.verify(movieReviewFeedbackService).getMovieReviewMovieReviewFeedback(movieReview.getId());
    }

    @Test
    public void testGetMovieReviewMovieReviewFeedbackWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieReviewFeedback.class,wrongId);
        Mockito.when(movieReviewFeedbackService.getMovieReviewMovieReviewFeedback(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get(
                "/api/v1/movie-reviews/{movieReviewId}/movie-review-feedbacks", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewMovieReviewFeedbackWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get(
                "/api/v1/movie-reviews/{movieReviewId}/movie-review-feedbacks",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMovieReviewMovieReviewFeedback() throws Exception {

        MovieReviewReadDTO movieReviewReadDTO = generateObject(MovieReviewReadDTO.class);
        MovieReviewFeedbackCreateDTO create = generateObject(MovieReviewFeedbackCreateDTO.class);

        MovieReviewFeedbackReadDTO read = createMovieReviewFeedbackRead(movieReviewReadDTO.getId());
        read.setPortalUserId(create.getPortalUserId());
        read.setMovieId(create.getMovieId());

        Mockito.when(movieReviewFeedbackService.createMovieReviewMovieReviewFeedback(movieReviewReadDTO.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/movie-reviews/{movieReviewId}/movie-review-feedbacks",
                movieReviewReadDTO.getId())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewFeedbackReadDTO actualMovieReviewMovieReviewFeedback = objectMapper
                .readValue(resultJson, MovieReviewFeedbackReadDTO.class);
        Assertions.assertThat(actualMovieReviewMovieReviewFeedback).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieReviewMovieReviewFeedback() throws Exception {

        MovieReviewReadDTO movieReviewReadDTO = generateObject(MovieReviewReadDTO.class);
        MovieReviewFeedbackPatchDTO patchDTO = generateObject(MovieReviewFeedbackPatchDTO.class);
        patchDTO.setMovieReviewId(movieReviewReadDTO.getId());

        MovieReviewFeedbackReadDTO read = createMovieReviewFeedbackRead(movieReviewReadDTO.getId());

        Mockito.when(movieReviewFeedbackService.patchMovieReviewMovieReviewFeedback(movieReviewReadDTO.getId(),
                read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch(
                "/api/v1/movie-reviews/{movieReviewId}/movie-review-feedbacks/{id}",
                movieReviewReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewFeedbackReadDTO actualMovieReviewMovieReviewFeedback = objectMapper
                .readValue(resultJson, MovieReviewFeedbackReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewMovieReviewFeedback);
    }

    @Test
    public void testDeleteMovieReviewMovieReviewFeedback() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/movie-reviews/{movieReviewId}/movie-review-feedbacks/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(movieReviewFeedbackService).deleteMovieReviewMovieReviewFeedback(id, id);
    }

    @Test
    public void testPutMovieReviewMovieReviewFeedback() throws Exception {

        MovieReviewReadDTO movieReviewReadDTO = generateObject(MovieReviewReadDTO.class);
        MovieReviewFeedbackPutDTO putDTO = generateObject(MovieReviewFeedbackPutDTO.class);
        putDTO.setMovieReviewId(movieReviewReadDTO.getId());

        MovieReviewFeedbackReadDTO read = createMovieReviewFeedbackRead(movieReviewReadDTO.getId());
        read.setMovieId(putDTO.getMovieId());
        read.setPortalUserId(putDTO.getPortalUserId());

        Mockito.when(movieReviewFeedbackService.updateMovieReviewMovieReviewFeedback(movieReviewReadDTO.getId(),
                read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/movie-reviews/{movieReviewId}/movie-review-feedbacks/{id}",
                movieReviewReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewFeedbackReadDTO actualMovieReviewMovieReviewFeedback = objectMapper
                .readValue(resultJson, MovieReviewFeedbackReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewMovieReviewFeedback);
    }

    @Test
    public void testCreateMovieReviewFeedbackValidationFailed() throws Exception {
        MovieReviewFeedbackCreateDTO create = new MovieReviewFeedbackCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/movie-reviews/{movieReviewId}/movie-review-feedbacks",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewFeedbackService,
                Mockito.never()).createMovieReviewMovieReviewFeedback(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieReviewFeedbackValidationFailed() throws Exception {
        MovieReviewFeedbackPutDTO put = new MovieReviewFeedbackPutDTO();

        String resultJson = mvc.perform(put("/api/v1/movie-reviews/{movieReviewId}/movie-review-feedbacks/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieReviewFeedbackService,
                Mockito.never()).updateMovieReviewMovieReviewFeedback(ArgumentMatchers.any(), ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    private MovieReviewFeedbackReadDTO createMovieReviewFeedbackRead(UUID movieReviewId) {
        MovieReviewFeedbackReadDTO movieReviewFeedback = generateObject(MovieReviewFeedbackReadDTO.class);
        movieReviewFeedback.setMovieReviewId(movieReviewId);
        return movieReviewFeedback;
    }
}
