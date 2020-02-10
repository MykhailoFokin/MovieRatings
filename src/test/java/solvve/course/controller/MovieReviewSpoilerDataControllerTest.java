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
import solvve.course.domain.MovieSpoilerData;
import solvve.course.dto.*;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.service.MovieSpoilerDataService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieReviewSpoilerDataController.class)
@ActiveProfiles("test")
public class MovieReviewSpoilerDataControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieSpoilerDataService movieSpoilerDataService;

    private MovieSpoilerDataReadDTO createMovieSpoilerDataRead(UUID movieReviewId) {
        MovieSpoilerDataReadDTO movieSpoilerData = new MovieSpoilerDataReadDTO();
        movieSpoilerData.setId(UUID.randomUUID());
        movieSpoilerData.setStartIndex(100);
        movieSpoilerData.setEndIndex(150);
        movieSpoilerData.setMovieReviewId(movieReviewId);
        return movieSpoilerData;
    }

    private MovieReviewReadDTO createMovieReview() {
        MovieReviewReadDTO movieReview = new MovieReviewReadDTO();
        movieReview.setId(UUID.randomUUID());
        movieReview.setTextReview("Review");
        return movieReview;
    }

    @Test
    public void testGetMovieSpoilerData() throws Exception {
        MovieReviewReadDTO movieReview = createMovieReview();
        List<MovieSpoilerDataReadDTO> movieSpoilerData = List.of(createMovieSpoilerDataRead(movieReview.getId()));

        Mockito.when(movieSpoilerDataService.getMovieReviewSpoilerDatas(movieReview.getId()))
                .thenReturn(movieSpoilerData);

        String resultJson = mvc.perform(get("/api/v1/moviereviews/{moviereviewid}/moviespoilerdatas",
                movieReview.getId().toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<MovieSpoilerDataReadDTO> actualMovie = objectMapper.readValue(resultJson,
                new TypeReference<List<MovieSpoilerDataReadDTO>>(){});
        Assertions.assertThat(actualMovie).isEqualTo(movieSpoilerData);

        Mockito.verify(movieSpoilerDataService).getMovieReviewSpoilerDatas(movieReview.getId());
    }

    @Test
    public void testGetMovieSpoilerDataWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(MovieSpoilerData.class,wrongId);
        Mockito.when(movieSpoilerDataService.getMovieReviewSpoilerDatas(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/moviereviews/{moviereviewid}/moviespoilerdatas"
                , wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testCreateMovieSpoilerData() throws Exception {

        MovieReviewReadDTO movieReviewReadDTO = createMovieReview();
        MovieSpoilerDataCreateDTO create = new MovieSpoilerDataCreateDTO();
        create.setStartIndex(100);
        create.setEndIndex(150);
        create.setMovieReviewId(movieReviewReadDTO.getId());

        MovieSpoilerDataReadDTO read = createMovieSpoilerDataRead(movieReviewReadDTO.getId());

        Mockito.when(movieSpoilerDataService.createMovieReviewSpoilerData(movieReviewReadDTO.getId(),
                create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviereviews/{moviereviewid}/moviespoilerdatas"
                , movieReviewReadDTO.getId().toString())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieSpoilerDataReadDTO actualMovieSpoilerData = objectMapper
                .readValue(resultJson, MovieSpoilerDataReadDTO.class);
        Assertions.assertThat(actualMovieSpoilerData).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieSpoilerData() throws Exception {

        MovieReviewReadDTO movieReviewReadDTO = createMovieReview();
        MovieSpoilerDataPatchDTO patchDTO = new MovieSpoilerDataPatchDTO();
        patchDTO.setStartIndex(100);
        patchDTO.setEndIndex(150);
        patchDTO.setMovieReviewId(movieReviewReadDTO.getId());

        MovieSpoilerDataReadDTO read = createMovieSpoilerDataRead(movieReviewReadDTO.getId());

        Mockito.when(movieSpoilerDataService.patchMovieReviewSpoilerData(movieReviewReadDTO.getId(),
                read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/moviereviews/{moviereviewid}/moviespoilerdatas/{id}",
                movieReviewReadDTO.getId(), read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieSpoilerDataReadDTO actualMovieSpoilerData = objectMapper
                .readValue(resultJson, MovieSpoilerDataReadDTO.class);
        Assert.assertEquals(read, actualMovieSpoilerData);
    }

    @Test
    public void testDeleteMovieSpoilerData() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/moviereviews/{moviereviewid}/moviespoilerdatas/{id}",
                id, id.toString())).andExpect(status().isOk());

        Mockito.verify(movieSpoilerDataService).deleteMovieReviewSpoilerData(id, id);
    }

    @Test
    public void testPutMovieSpoilerData() throws Exception {

        MovieReviewReadDTO movieReviewReadDTO = createMovieReview();
        MovieSpoilerDataPutDTO putDTO = new MovieSpoilerDataPutDTO();
        putDTO.setStartIndex(100);
        putDTO.setEndIndex(150);
        putDTO.setMovieReviewId(movieReviewReadDTO.getId());

        MovieSpoilerDataReadDTO read = createMovieSpoilerDataRead(movieReviewReadDTO.getId());

        Mockito.when(movieSpoilerDataService.updateMovieReviewSpoilerData(movieReviewReadDTO.getId(),
                read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviereviews/{moviereviewid}/moviespoilerdatas/{id}",
                movieReviewReadDTO.getId().toString(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieSpoilerDataReadDTO actualMovieSpoilerData = objectMapper
                .readValue(resultJson, MovieSpoilerDataReadDTO.class);
        Assert.assertEquals(read, actualMovieSpoilerData);
    }
}
