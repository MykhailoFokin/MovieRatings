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
import solvve.course.domain.MovieSpoilerData;
import solvve.course.dto.MovieSpoilerDataCreateDTO;
import solvve.course.dto.MovieSpoilerDataReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieSpoilerDataService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieSpoilerDataController.class)
@ActiveProfiles("test")
public class MovieSpoilerDataControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieSpoilerDataService movieSpoilerDataService;

    @Test
    public void testGetMovieSpoilerData() throws Exception {
        MovieSpoilerDataReadDTO movieSpoilerData = new MovieSpoilerDataReadDTO();
        movieSpoilerData.setId(UUID.randomUUID());
        //movieSpoilerData.setMovieReviewId(movieReviewReadDTO.getId());
        movieSpoilerData.setStartIndex(100);
        movieSpoilerData.setEndIndex(150);

        Mockito.when(movieSpoilerDataService.getMovieSpoilerData(movieSpoilerData.getId())).thenReturn(movieSpoilerData);

        String resultJson = mvc.perform(get("/api/v1/moviespoilerdata/{id}", movieSpoilerData.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        System.out.println(resultJson);
        MovieSpoilerDataReadDTO actualMovie = objectMapper.readValue(resultJson, MovieSpoilerDataReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(movieSpoilerData);

        Mockito.verify(movieSpoilerDataService).getMovieSpoilerData(movieSpoilerData.getId());
    }

    @Test
    public void testGetMovieSpoilerDataWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieSpoilerData.class,wrongId);
        Mockito.when(movieSpoilerDataService.getMovieSpoilerData(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/moviespoilerdata/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieSpoilerDataWrongFormatId() throws Exception {
        String wrongId = "123";

        IllegalArgumentException exception = new IllegalArgumentException("id should be of type java.util.UUID");

        String resultJson = mvc.perform(get("/api/v1/moviespoilerdata/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateMovieSpoilerData() throws Exception {

        MovieSpoilerDataCreateDTO create = new MovieSpoilerDataCreateDTO();
        //create.setMovieReviewId(movieReviewReadDTO.getId());
        create.setStartIndex(100);
        create.setEndIndex(150);

        MovieSpoilerDataReadDTO read = new MovieSpoilerDataReadDTO();
        read.setId(UUID.randomUUID());
        //read.setMovieReviewId(movieReviewReadDTO.getId());
        read.setStartIndex(100);
        read.setEndIndex(150);

        Mockito.when(movieSpoilerDataService.createMovieSpoilerData(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviespoilerdata")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieSpoilerDataReadDTO actualMovieSpoilerData = objectMapper.readValue(resultJson, MovieSpoilerDataReadDTO.class);
        Assertions.assertThat(actualMovieSpoilerData).isEqualToComparingFieldByField(read);
    }
}
