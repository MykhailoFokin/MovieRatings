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
import solvve.course.domain.MovieReview;
import solvve.course.dto.MovieReviewCreateDTO;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.dto.UserModeratedStatusType;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieReviewService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieReviewController.class)
@ActiveProfiles("test")
public class MovieReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieReviewService movieReviewService;

    @Test
    public void testGetMovieReview() throws Exception {
        MovieReviewReadDTO movieReview = new MovieReviewReadDTO();
        movieReview.setId(UUID.randomUUID());
        //movieReview.setUserId(portalUserReadDTO.getId());
        //movieReview.setMovieId(movieReadDTO.getId());
        movieReview.setTextReview("This movie can be described as junk.");
        movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //movieReview.setModeratorId(portalUserReadDTO.getId());

        Mockito.when(movieReviewService.getMovieReview(movieReview.getId())).thenReturn(movieReview);

        String resultJson = mvc.perform(get("/api/v1/moviereview/{id}", movieReview.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        MovieReviewReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(movieReview);

        Mockito.verify(movieReviewService).getMovieReview(movieReview.getId());
    }

    @Test
    public void testGetMovieReviewWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieReview.class,wrongId);
        Mockito.when(movieReviewService.getMovieReview(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/moviereview/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieReviewWrongFormatId() throws Exception {
        String wrongId = "123";

        IllegalArgumentException exception = new IllegalArgumentException("id should be of type java.util.UUID");

        String resultJson = mvc.perform(get("/api/v1/moviereview/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateMovieReview() throws Exception {

        MovieReviewCreateDTO create = new MovieReviewCreateDTO();
        //create.setUserId(portalUserReadDTO.getId());
        //create.setMovieId(movieReadDTO.getId());
        create.setTextReview("This movie can be described as junk.");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //create.setModeratorId(portalUserReadDTO.getId());

        MovieReviewReadDTO read = new MovieReviewReadDTO();
        read.setId(UUID.randomUUID());
        //read.setUserId(portalUserReadDTO.getId());
        //read.setMovieId(movieReadDTO.getId());
        read.setTextReview("This movie can be described as junk.");
        read.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        //read.setModeratorId(portalUserReadDTO.getId());

        Mockito.when(movieReviewService.createMovieReview(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviereview")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewReadDTO actualMovieReview = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assertions.assertThat(actualMovieReview).isEqualToComparingFieldByField(read);
    }
}
