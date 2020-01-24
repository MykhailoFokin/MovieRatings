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
import solvve.course.dto.MovieReviewPatchDTO;
import solvve.course.dto.MovieReviewPutDTO;
import solvve.course.dto.MovieReviewReadDTO;
import solvve.course.domain.UserModeratedStatusType;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieReviewService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private MovieReviewReadDTO createMovieReviewRead() {
        MovieReviewReadDTO movieReview = new MovieReviewReadDTO();
        movieReview.setId(UUID.randomUUID());
        movieReview.setTextReview("This movie can be described as junk.");
        movieReview.setModeratedStatus(UserModeratedStatusType.SUCCESS);
        return movieReview;
    }

    @Test
    public void testGetMovieReview() throws Exception {
        MovieReviewReadDTO movieReview = createMovieReviewRead();

        Mockito.when(movieReviewService.getMovieReview(movieReview.getId())).thenReturn(movieReview);

        String resultJson = mvc.perform(get("/api/v1/moviereviews/{id}", movieReview.getId()))
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

        MovieReviewCreateDTO create = new MovieReviewCreateDTO();
        create.setTextReview("This movie can be described as junk.");
        create.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        MovieReviewReadDTO read = createMovieReviewRead();

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

        MovieReviewPatchDTO patchDTO = new MovieReviewPatchDTO();
        patchDTO.setTextReview("This movie can be described as junk.");
        patchDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        MovieReviewReadDTO read = createMovieReviewRead();

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

        MovieReviewPutDTO putDTO = new MovieReviewPutDTO();
        putDTO.setTextReview("This movie can be described as junk.");
        putDTO.setModeratedStatus(UserModeratedStatusType.SUCCESS);

        MovieReviewReadDTO read = createMovieReviewRead();

        Mockito.when(movieReviewService.putMovieReview(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviereviews/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReviewReadDTO actualMovieReview = objectMapper.readValue(resultJson, MovieReviewReadDTO.class);
        Assert.assertEquals(read, actualMovieReview);
    }
}
