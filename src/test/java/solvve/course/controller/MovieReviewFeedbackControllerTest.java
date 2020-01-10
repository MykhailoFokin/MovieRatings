package solvve.course.controller;

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
import solvve.course.dto.MovieReviewFeedbackCreateDTO;
import solvve.course.dto.MovieReviewFeedbackReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieReviewFeedbackService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieReviewFeedbackController.class)
@ActiveProfiles("test")
public class MovieReviewFeedbackControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieReviewFeedbackService movieReviewFeedbackService;

    @Test
    public void testGetMovieReviewFeedback() throws Exception {
        MovieReviewFeedbackReadDTO movieReviewFeedback = new MovieReviewFeedbackReadDTO();
        movieReviewFeedback.setId(UUID.randomUUID());
        //movieReviewFeedback.setUserId(portalUserReadDTO.getId());
        //movieReviewFeedback.setMovieId(movieReadDTO.getId());
        //movieReviewFeedback.setMovieReviewId(movieReviewReadDTO.getId());
        movieReviewFeedback.setIsLiked(true);

        Mockito.when(movieReviewFeedbackService.getMovieReviewFeedback(movieReviewFeedback.getId())).thenReturn(movieReviewFeedback);

        String resultJson = mvc.perform(get("/api/v1/moviereviewfeedback/{id}", movieReviewFeedback.getId()))
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

        String resultJson = mvc.perform(get("/api/v1/moviereviewfeedback/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewFeedbackWrongFormatId() throws Exception {
        String wrongId = "123";

        IllegalArgumentException exception = new IllegalArgumentException("id should be of type java.util.UUID");

        String resultJson = mvc.perform(get("/api/v1/moviereviewfeedback/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateMovieReviewFeedback() throws Exception {

        MovieReviewFeedbackCreateDTO create = new MovieReviewFeedbackCreateDTO();
        //create.setUserId(portalUserReadDTO.getId());
        //create.setMovieId(movieReadDTO.getId());
        //create.setMovieReviewId(movieReviewReadDTO.getId());
        create.setIsLiked(true);

        MovieReviewFeedbackReadDTO read = new MovieReviewFeedbackReadDTO();
        read.setId(UUID.randomUUID());
        //read.setUserId(portalUserReadDTO.getId());
        //read.setMovieId(movieReadDTO.getId());
        //read.setMovieReviewId(movieReviewReadDTO.getId());
        read.setIsLiked(true);

        Mockito.when(movieReviewFeedbackService.createMovieReviewFeedback(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviereviewfeedback")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewFeedbackReadDTO actualMovieReviewFeedback = objectMapper.readValue(resultJson, MovieReviewFeedbackReadDTO.class);
        Assertions.assertThat(actualMovieReviewFeedback).isEqualToComparingFieldByField(read);
    }
}
