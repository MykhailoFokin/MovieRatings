package solvve.course.controller;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import solvve.course.domain.MovieSpoilerData;
import solvve.course.dto.MovieSpoilerDataCreateDTO;
import solvve.course.dto.MovieSpoilerDataPatchDTO;
import solvve.course.dto.MovieSpoilerDataPutDTO;
import solvve.course.dto.MovieSpoilerDataReadDTO;
import solvve.course.exception.ControllerValidationException;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.MovieSpoilerDataService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieSpoilerDataController.class)
public class MovieSpoilerDataControllerTest extends BaseControllerTest {

    @MockBean
    private MovieSpoilerDataService movieSpoilerDataService;

    @Test
    public void testGetMovieSpoilerData() throws Exception {
        MovieSpoilerDataReadDTO movieSpoilerData = generateObject(MovieSpoilerDataReadDTO.class);

        Mockito.when(movieSpoilerDataService.getMovieSpoilerData(movieSpoilerData.getId()))
                .thenReturn(movieSpoilerData);

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

        String resultJson = mvc.perform(get("/api/v1/moviespoilerdata/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMovieSpoilerData() throws Exception {

        MovieSpoilerDataCreateDTO create = generateObject(MovieSpoilerDataCreateDTO.class);

        MovieSpoilerDataReadDTO read = generateObject(MovieSpoilerDataReadDTO.class);
        read.setMovieReviewId(create.getMovieReviewId());

        Mockito.when(movieSpoilerDataService.createMovieSpoilerData(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/moviespoilerdata")
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

        MovieSpoilerDataPatchDTO patchDTO = generateObject(MovieSpoilerDataPatchDTO.class);

        MovieSpoilerDataReadDTO read = generateObject(MovieSpoilerDataReadDTO.class);

        Mockito.when(movieSpoilerDataService.patchMovieSpoilerData(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/moviespoilerdata/{id}", read.getId().toString())
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

        mvc.perform(delete("/api/v1/moviespoilerdata/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(movieSpoilerDataService).deleteMovieSpoilerData(id);
    }

    @Test
    public void testPutMovieSpoilerData() throws Exception {

        MovieSpoilerDataPutDTO putDTO = generateObject(MovieSpoilerDataPutDTO.class);

        MovieSpoilerDataReadDTO read = generateObject(MovieSpoilerDataReadDTO.class);
        read.setMovieReviewId(putDTO.getMovieReviewId());

        Mockito.when(movieSpoilerDataService.updateMovieSpoilerData(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/moviespoilerdata/{id}", read.getId().toString())
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

        String resultJson = mvc.perform(post("/api/v1/moviespoilerdata")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieSpoilerDataService, Mockito.never()).createMovieSpoilerData(ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieSpoilerDataValidationFailed() throws Exception {
        MovieSpoilerDataPutDTO put = new MovieSpoilerDataPutDTO();

        String resultJson = mvc.perform(put("/api/v1/moviespoilerdata/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieSpoilerDataService, Mockito.never()).updateMovieSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateMovieSpoilerDataWrongIndexes() throws Exception {
        MovieSpoilerDataCreateDTO movieSpoilerDataCreate = generateObject(MovieSpoilerDataCreateDTO.class);

        String resultJson = mvc.perform(post("/api/v1//moviespoilerdata")
                .content(objectMapper.writeValueAsString(movieSpoilerDataCreate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(movieSpoilerDataService, Mockito.never()).createMovieSpoilerData(ArgumentMatchers.any());
    }

    @Test
    public void testPatchMovieSpoilerDataWrongIndexes() throws Exception {
        MovieSpoilerDataPatchDTO movieSpoilerDataPatch = generateObject(MovieSpoilerDataPatchDTO.class);
        movieSpoilerDataPatch.setStartIndex(999);
        movieSpoilerDataPatch.setEndIndex(1);

        String resultJson = mvc.perform(patch("/api/v1/moviespoilerdata/{id}",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(movieSpoilerDataPatch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(movieSpoilerDataService, Mockito.never()).patchMovieSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieSpoilerDataWrongIndexes() throws Exception {
        MovieSpoilerDataPutDTO movieSpoilerDataPut = generateObject(MovieSpoilerDataPutDTO.class);

        String resultJson = mvc.perform(put("/api/v1//moviespoilerdata/{id}",
                UUID.randomUUID())
                .content(objectMapper.writeValueAsString(movieSpoilerDataPut))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        ErrorInfo errorInfo = objectMapper.readValue(resultJson, ErrorInfo.class);
        Assert.assertTrue(errorInfo.getMessage().contains("startIndex"));
        Assert.assertTrue(errorInfo.getMessage().contains("endIndex"));
        Assert.assertEquals(ControllerValidationException.class, errorInfo.getExceptionClass());

        Mockito.verify(movieSpoilerDataService, Mockito.never()).updateMovieSpoilerData(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
