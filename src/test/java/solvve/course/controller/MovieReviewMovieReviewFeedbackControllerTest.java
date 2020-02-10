package solvve.course.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import solvve.course.domain.MovieReviewFeedback;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieReviewFeedbackService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieReviewMovieReviewFeedbackController.class)
@ActiveProfiles("test")
public class MovieReviewMovieReviewFeedbackControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieReviewFeedbackService movieReviewFeedbackService;

    @Test
    public void testGetMovieReviewMovieReviewFeedback() throws Exception {
        MovieReviewReadDTO movieReview = createMovieReview();
        List<MovieReviewFeedbackReadDTO> movieReviewFeedbackReadDTOList =
                List.of(createMovieReviewFeedbackRead(movieReview.getId()));

        Mockito.when(movieReviewFeedbackService.getMovieReviewMovieReviewFeedback(movieReview.getId()))
                .thenReturn(movieReviewFeedbackReadDTOList);

        String resultJson = mvc.perform(get("/api/v1/moviereviews/{moviereviewid}/moviereviewfeedbacks",
                movieReview.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieReviewFeedbackReadDTO> actualMovie = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieReviewFeedbackReadDTO>>(){});
        Assertions.assertThat(actualMovie).isEqualTo(movieReviewFeedbackReadDTOList);

        Mockito.verify(movieReviewFeedbackService).getMovieReviewMovieReviewFeedback(movieReview.getId());
    }

    @Test
    public void testGetMovieReviewMovieReviewFeedbackWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieReviewFeedback.class,wrongId);
        Mockito.when(movieReviewFeedbackService.getMovieReviewMovieReviewFeedback(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get(
                "/api/v1/moviereviews/{moviereviewid}/moviereviewfeedbacks", wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewMovieReviewFeedbackWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get(
                "/api/v1/moviereviews/{moviereviewid}/moviereviewfeedbacks",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMovieReviewMovieReviewFeedback() throws Exception {

        MovieReviewReadDTO movieReviewReadDTO = createMovieReview();
        MovieReviewFeedbackCreateDTO create = new MovieReviewFeedbackCreateDTO();
        create.setIsLiked(true);
        create.setMovieReviewId(movieReviewReadDTO.getId());

        MovieReviewFeedbackReadDTO read = createMovieReviewFeedbackRead(movieReviewReadDTO.getId());

        Mockito.when(movieReviewFeedbackService.createMovieReviewMovieReviewFeedback(movieReviewReadDTO.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviereviews/{moviereviewid}/moviereviewfeedbacks",
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

        MovieReviewReadDTO movieReviewReadDTO = createMovieReview();
        MovieReviewFeedbackPatchDTO patchDTO = new MovieReviewFeedbackPatchDTO();
        patchDTO.setIsLiked(true);
        patchDTO.setMovieReviewId(movieReviewReadDTO.getId());

        MovieReviewFeedbackReadDTO read = createMovieReviewFeedbackRead(movieReviewReadDTO.getId());

        Mockito.when(movieReviewFeedbackService.patchMovieReviewMovieReviewFeedback(movieReviewReadDTO.getId(),
                read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch(
                "/api/v1/moviereviews/{moviereviewid}/moviereviewfeedbacks/{id}",
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

        mvc.perform(delete("/api/v1/moviereviews/{moviereviewid}/moviereviewfeedbacks/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(movieReviewFeedbackService).deleteMovieReviewMovieReviewFeedback(id, id);
    }

    @Test
    public void testPutMovieReviewMovieReviewFeedback() throws Exception {

        MovieReviewReadDTO movieReviewReadDTO = createMovieReview();
        MovieReviewFeedbackPutDTO putDTO = new MovieReviewFeedbackPutDTO();
        putDTO.setIsLiked(true);
        putDTO.setMovieReviewId(movieReviewReadDTO.getId());

        MovieReviewFeedbackReadDTO read = createMovieReviewFeedbackRead(movieReviewReadDTO.getId());

        Mockito.when(movieReviewFeedbackService.updateMovieReviewMovieReviewFeedback(movieReviewReadDTO.getId(),
                read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviereviews/{moviereviewid}/moviereviewfeedbacks/{id}",
                movieReviewReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewFeedbackReadDTO actualMovieReviewMovieReviewFeedback = objectMapper
                .readValue(resultJson, MovieReviewFeedbackReadDTO.class);
        Assert.assertEquals(read, actualMovieReviewMovieReviewFeedback);
    }

    private MovieReviewFeedbackReadDTO createMovieReviewFeedbackRead(UUID movieReviewId) {
        MovieReviewFeedbackReadDTO movieReviewFeedback = new MovieReviewFeedbackReadDTO();
        movieReviewFeedback.setId(UUID.randomUUID());
        movieReviewFeedback.setIsLiked(true);
        movieReviewFeedback.setMovieReviewId(movieReviewId);
        return movieReviewFeedback;
    }

    private MovieReviewReadDTO createMovieReview() {
        MovieReviewReadDTO movieReview = new MovieReviewReadDTO();
        movieReview.setId(UUID.randomUUID());
        movieReview.setTextReview("Review");
        return movieReview;
    }
}
