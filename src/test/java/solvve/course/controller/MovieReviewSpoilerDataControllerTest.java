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
import solvve.course.domain.MovieSpoilerData;
import solvve.course.dto.*;
import solvve.course.exception.ControllerValidationException;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.MovieReviewSpoilerDataService;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieReviewSpoilerDataController.class)
public class MovieReviewSpoilerDataControllerTest extends BaseControllerTest {

    @MockBean
    private MovieReviewSpoilerDataService movieSpoilerDataService;

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

        String resultJson = mvc.perform(get("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas",
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

        String resultJson = mvc.perform(get("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas"
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

        String resultJson = mvc.perform(post("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas"
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

        String resultJson = mvc.perform(patch("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas/{id}",
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

        mvc.perform(delete("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas/{id}",
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

        String resultJson = mvc.perform(put("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas/{id}",
                movieReviewReadDTO.getId().toString(), read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieSpoilerDataReadDTO actualMovieSpoilerData = objectMapper
                .readValue(resultJson, MovieSpoilerDataReadDTO.class);
        Assert.assertEquals(read, actualMovieSpoilerData);
    }

    @Test
    public void testCreateMovieSpoilerDataValidationFailed() throws Exception {
        MovieSpoilerDataCreateDTO create = new MovieSpoilerDataCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieSpoilerDataService, Mockito.never()).createMovieReviewSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieSpoilerDataValidationFailed() throws Exception {
        MovieSpoilerDataPutDTO put = new MovieSpoilerDataPutDTO();

        String resultJson = mvc.perform(put("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieSpoilerDataService, Mockito.never()).updateMovieReviewSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testCreateMovieSpoilerDataWrongIndexes() throws Exception {
        MovieSpoilerDataCreateDTO movieSpoilerDataCreate = new MovieSpoilerDataCreateDTO();
        movieSpoilerDataCreate.setMovieReviewId(UUID.randomUUID());
        movieSpoilerDataCreate.setStartIndex(100);
        movieSpoilerDataCreate.setEndIndex(10);

        String resultJson = mvc.perform(post("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(movieSpoilerDataCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(movieSpoilerDataService, Mockito.never()).createMovieReviewSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchMovieSpoilerDataWrongIndexes() throws Exception {
        MovieSpoilerDataPatchDTO movieSpoilerDataPatch = new MovieSpoilerDataPatchDTO();
        movieSpoilerDataPatch.setMovieReviewId(UUID.randomUUID());
        movieSpoilerDataPatch.setStartIndex(100);
        movieSpoilerDataPatch.setEndIndex(10);

        String resultJson = mvc.perform(patch("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(movieSpoilerDataPatch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(movieSpoilerDataService, Mockito.never()).patchMovieReviewSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieSpoilerDataWrongIndexes() throws Exception {
        MovieSpoilerDataPutDTO movieSpoilerDataPut = new MovieSpoilerDataPutDTO();
        movieSpoilerDataPut.setMovieReviewId(UUID.randomUUID());
        movieSpoilerDataPut.setStartIndex(100);
        movieSpoilerDataPut.setEndIndex(10);

        String resultJson = mvc.perform(put("/api/v1/movie-reviews/{movieReviewId}/movie-spoiler-datas/{id}",
                UUID.randomUUID(), UUID.randomUUID())
                .content(objectMapper.writeValueAsString(movieSpoilerDataPut))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(movieSpoilerDataService, Mockito.never()).updateMovieReviewSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}
