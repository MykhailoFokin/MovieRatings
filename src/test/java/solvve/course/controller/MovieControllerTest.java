package solvve.course.controller;

import liquibase.util.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import solvve.course.domain.Movie;
import solvve.course.dto.MovieCreateDTO;
import solvve.course.dto.MoviePatchDTO;
import solvve.course.dto.MoviePutDTO;
import solvve.course.dto.MovieReadDTO;
import solvve.course.exception.EntityNotFoundException;
import solvve.course.exception.handler.ErrorInfo;
import solvve.course.service.MovieService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@WebMvcTest(controllers = MovieController.class)
public class MovieControllerTest extends BaseControllerTest {

    @MockBean
    private MovieService movieService;

    @Test
    public void testGetMovie() throws Exception {

        MovieReadDTO movie = generateObject(MovieReadDTO.class);

        Mockito.when(movieService.getMovie(movie.getId())).thenReturn(movie);

        String resultJson = mvc.perform(get("/api/v1/movies/{id}", movie.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(movie);

        Mockito.verify(movieService).getMovie(movie.getId());
    }

    @Test
    public void testGetMovieWrongId() throws Exception {
        UUID wrongId = UUID.randomUUID();

        EntityNotFoundException exception = new EntityNotFoundException(Movie.class,wrongId);
        Mockito.when(movieService.getMovie(wrongId)).thenThrow(exception);

        String resultJson = mvc.perform(get("/api/v1/movies/{id}",wrongId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains(exception.getMessage()));
    }

    @Test
    public void testGetMovieWrongFormatId() throws Exception {
        String wrongId = "123";

        String resultJson = mvc.perform(get("/api/v1/movies/{id}",wrongId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        Assert.assertTrue(resultJson.contains("Invalid UUID string"));
    }

    @Test
    public void testCreateMovie() throws Exception {

        MovieCreateDTO create = generateObject(MovieCreateDTO.class);

        MovieReadDTO read = generateObject(MovieReadDTO.class);

        Mockito.when(movieService.createMovie(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/movies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovie() throws Exception {

        MoviePatchDTO patchDTO = generateObject(MoviePatchDTO.class);
        patchDTO.setDescription("DescriptionMinimal");

        MovieReadDTO read = generateObject(MovieReadDTO.class);

        Mockito.when(movieService.patchMovie(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/movies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assert.assertEquals(read, actualMovie);
    }

    @Test
    public void testDeleteMovie() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/movies/{id}",id.toString())).andExpect(status().isOk());

        Mockito.verify(movieService).deleteMovie(id);
    }

    @Test
    public void testPutMovie() throws Exception {

        MoviePutDTO putDTO = generateObject(MoviePutDTO.class);
        putDTO.setDescription("DescriptionMinimal");

        MovieReadDTO read = generateObject(MovieReadDTO.class);

        Mockito.when(movieService.updateMovie(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/movies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assert.assertEquals(read, actualMovie);
    }

    @Test
    public void testCreateMovieValidationFailed() throws Exception {
        MovieCreateDTO create = new MovieCreateDTO();

        String resultJson = mvc.perform(post("/api/v1/movies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieService, Mockito.never()).createMovie(ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieValidationFailed() throws Exception {
        MoviePutDTO put = new MoviePutDTO();
        put.setCamera(StringUtils.repeat("*", 500));

        String resultJson = mvc.perform(put("/api/v1/movies/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieService, Mockito.never()).updateMovie(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void testPutMovieCheckLimitBorders() throws Exception {

        MoviePutDTO putDTO = generateObject(MoviePutDTO.class);
        putDTO.setAspectRatio(StringUtils.repeat("*", 1));
        putDTO.setCamera(StringUtils.repeat("*", 1));
        putDTO.setColour(StringUtils.repeat("*", 1));
        putDTO.setCritique(StringUtils.repeat("*", 1));
        putDTO.setDescription(StringUtils.repeat("*", 11));
        putDTO.setLaboratory(StringUtils.repeat("*", 1));
        putDTO.setSoundMix(StringUtils.repeat("*", 1));

        MovieReadDTO read = generateObject(MovieReadDTO.class);

        Mockito.when(movieService.updateMovie(read.getId(),putDTO)).thenReturn(read);

        String resultJson = mvc.perform(put("/api/v1/movies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assert.assertEquals(read, actualMovie);

        // Check upper border
        putDTO.setAspectRatio(StringUtils.repeat("*", 255));
        putDTO.setCamera(StringUtils.repeat("*", 255));
        putDTO.setColour(StringUtils.repeat("*", 255));
        putDTO.setCritique(StringUtils.repeat("*", 255));
        putDTO.setDescription(StringUtils.repeat("*", 1000));
        putDTO.setLaboratory(StringUtils.repeat("*", 255));
        putDTO.setSoundMix(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(put("/api/v1/movies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(putDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assert.assertEquals(read, actualMovie);
    }

    @Test
    public void testPutCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MoviePutDTO put = generateObject(MoviePutDTO.class);
        put.setAspectRatio("");
        put.setCamera("");
        put.setColour("");
        put.setCritique("");
        put.setDescription("");
        put.setLaboratory("");
        put.setSoundMix("");

        String resultJson = mvc.perform(put("/api/v1/movies/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieService, Mockito.never()).updateMovie(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPutCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MoviePutDTO put = generateObject(MoviePutDTO.class);
        put.setAspectRatio(StringUtils.repeat("*", 256));
        put.setCamera(StringUtils.repeat("*", 256));
        put.setColour(StringUtils.repeat("*", 256));
        put.setCritique(StringUtils.repeat("*", 256));
        put.setDescription(StringUtils.repeat("*", 1001));
        put.setLaboratory(StringUtils.repeat("*", 256));
        put.setSoundMix(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(put("/api/v1/movies/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(put))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieService, Mockito.never()).updateMovie(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MovieCreateDTO create = generateObject(MovieCreateDTO.class);
        create.setTitle("");
        create.setAspectRatio("");
        create.setCamera("");
        create.setColour("");
        create.setCritique("");
        create.setDescription("");
        create.setLaboratory("");
        create.setSoundMix("");

        String resultJson = mvc.perform(post("/api/v1/movies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieService, Mockito.never()).createMovie(ArgumentMatchers.any());
    }

    @Test
    public void testCreateCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MovieCreateDTO create = generateObject(MovieCreateDTO.class);
        create.setTitle(StringUtils.repeat("*", 256));
        create.setAspectRatio(StringUtils.repeat("*", 256));
        create.setCamera(StringUtils.repeat("*", 256));
        create.setColour(StringUtils.repeat("*", 256));
        create.setCritique(StringUtils.repeat("*", 256));
        create.setDescription(StringUtils.repeat("*", 1001));
        create.setLaboratory(StringUtils.repeat("*", 256));
        create.setSoundMix(StringUtils.repeat("*", 256));


        String resultJson = mvc.perform(post("/api/v1/movies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieService, Mockito.never()).createMovie(ArgumentMatchers.any());
    }

    @Test
    public void testCreateMovieCheckStingBorders() throws Exception {

        MovieCreateDTO create = generateObject(MovieCreateDTO.class);
        create.setTitle(StringUtils.repeat("*", 1));
        create.setAspectRatio(StringUtils.repeat("*", 1));
        create.setCamera(StringUtils.repeat("*", 1));
        create.setColour(StringUtils.repeat("*", 1));
        create.setCritique(StringUtils.repeat("*", 1));
        create.setDescription(StringUtils.repeat("*", 1));
        create.setLaboratory(StringUtils.repeat("*", 1));
        create.setSoundMix(StringUtils.repeat("*", 1));

        MovieReadDTO read = generateObject(MovieReadDTO.class);

        Mockito.when(movieService.createMovie(create)).thenReturn(read);

        String resultJson = mvc.perform(post("/api/v1/movies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(read);

        create.setTitle(StringUtils.repeat("*", 255));
        create.setAspectRatio(StringUtils.repeat("*", 255));
        create.setCamera(StringUtils.repeat("*", 255));
        create.setColour(StringUtils.repeat("*", 255));
        create.setCritique(StringUtils.repeat("*", 255));
        create.setDescription(StringUtils.repeat("*", 1000));
        create.setLaboratory(StringUtils.repeat("*", 255));
        create.setSoundMix(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(post("/api/v1/movies")
                .content(objectMapper.writeValueAsString(create))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assertions.assertThat(actualMovie).isEqualToComparingFieldByField(read);
    }

    @Test
    public void testPatchMovieCheckStringBorders() throws Exception {

        MoviePatchDTO patchDTO = generateObject(MoviePatchDTO.class);
        patchDTO.setTitle(StringUtils.repeat("*", 1));
        patchDTO.setAspectRatio(StringUtils.repeat("*", 1));
        patchDTO.setCamera(StringUtils.repeat("*", 1));
        patchDTO.setColour(StringUtils.repeat("*", 1));
        patchDTO.setCritique(StringUtils.repeat("*", 1));
        patchDTO.setDescription(StringUtils.repeat("*", 10));
        patchDTO.setLaboratory(StringUtils.repeat("*", 1));
        patchDTO.setSoundMix(StringUtils.repeat("*", 1));

        MovieReadDTO read = generateObject(MovieReadDTO.class);

        Mockito.when(movieService.patchMovie(read.getId(),patchDTO)).thenReturn(read);

        String resultJson = mvc.perform(patch("/api/v1/movies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MovieReadDTO actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assert.assertEquals(read, actualMovie);

        patchDTO.setTitle(StringUtils.repeat("*", 255));
        patchDTO.setAspectRatio(StringUtils.repeat("*", 255));
        patchDTO.setCamera(StringUtils.repeat("*", 255));
        patchDTO.setColour(StringUtils.repeat("*", 255));
        patchDTO.setCritique(StringUtils.repeat("*", 255));
        patchDTO.setDescription(StringUtils.repeat("*", 1000));
        patchDTO.setLaboratory(StringUtils.repeat("*", 255));
        patchDTO.setSoundMix(StringUtils.repeat("*", 255));

        resultJson = mvc.perform(patch("/api/v1/movies/{id}", read.getId().toString())
                .content(objectMapper.writeValueAsString(patchDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        actualMovie = objectMapper.readValue(resultJson, MovieReadDTO.class);
        Assert.assertEquals(read, actualMovie);
    }

    @Test
    public void testPatchCompanyDetailDescriptionEmptyValidationFailed() throws Exception {
        MoviePatchDTO patch = generateObject(MoviePatchDTO.class);
        patch.setTitle("");
        patch.setAspectRatio("");
        patch.setCamera("");
        patch.setColour("");
        patch.setCritique("");
        patch.setDescription("");
        patch.setLaboratory("");
        patch.setSoundMix("");

        String resultJson = mvc.perform(patch("/api/v1/movies/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieService, Mockito.never()).patchMovie(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }

    @Test
    public void testPatchCompanyDetailDescriptionLimitValidationFailed() throws Exception {
        MoviePatchDTO patch = generateObject(MoviePatchDTO.class);
        patch.setTitle(StringUtils.repeat("*", 256));
        patch.setAspectRatio(StringUtils.repeat("*", 256));
        patch.setCamera(StringUtils.repeat("*", 256));
        patch.setColour(StringUtils.repeat("*", 256));
        patch.setCritique(StringUtils.repeat("*", 256));
        patch.setDescription(StringUtils.repeat("*", 1001));
        patch.setLaboratory(StringUtils.repeat("*", 256));
        patch.setSoundMix(StringUtils.repeat("*", 256));

        String resultJson = mvc.perform(patch("/api/v1/movies/{id}", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(patch))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        objectMapper.readValue(resultJson, ErrorInfo.class);
        Mockito.verify(movieService, Mockito.never()).patchMovie(ArgumentMatchers.any(),
                ArgumentMatchers.any());
    }
}
